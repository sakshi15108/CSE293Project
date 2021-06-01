package ZUC_128

import chisel3._
import chisel3.util._

import scala.collection.mutable.ArrayBuffer


object  zuc128 {
  /** FSM states **/
  val idle :: loadKey :: initMode :: workMode :: genKeystream :: Nil = Enum(5)

  /** Helper functions **/
  /* c = a + b mod (2^31 – 1) */
  def AddM(a: UInt, b: UInt): UInt = {
    ((a + b)(30,0)) +& ((a + b) >> 31).asUInt()
  }
  def MulByPow2(x: UInt, k: Int): UInt = {
    ((x << k) | (x >> (31 - k))) & zuc128_model.MASK.asUInt()
  }
  def ROT(a: SInt, k: Int): SInt = {
    (((a << k)(31,0)) | (a >> (32 - k)).asUInt()).asSInt()
  }

  /**Both L1 and L2 are linear transforms from 32-bit words to 32-bit words*/
  def L1(x: UInt): SInt = {
    (x.asSInt() ^ ROT(x.asSInt(), 2) ^ ROT(x.asSInt(), 10) ^ ROT(x.asSInt(), 18) ^ ROT(x.asSInt(), 24))
  }
  def L2(X: UInt): SInt = {
    (X.asSInt() ^ ROT(X.asSInt(), 8) ^ ROT(X.asSInt(), 14) ^ ROT(X.asSInt(), 22) ^ ROT(X.asSInt(), 30))
  }
  def MAKEU32(a:UInt, b: UInt, c: UInt, d: UInt): UInt = {
    ((a << 24) | (b << 16) | (c << 8) | (d))
  }
  def MAKEU31(a: UInt, b: UInt, c: UInt):UInt = {
    ((a << 23) | (b << 8) | c).asUInt()
  }
}

/** Parameters to define the size of Input and Output ports size
 * can be modified if we need to implement for different Input Key size (ex: 256 bit for ZUC256)
 * also by providing different KSlen value, KeyStream of that many words can be generated as Output*/
case class zucParams(KSlen: Int,Key_len :Int,parallelism :Int) {
  val KeyStream_wordsize = 32
  val keys_num = Key_len
  val KStreamlen = KSlen
  val load_cycles = keys_num/parallelism
  assert(keys_num%load_cycles==0, "CHECK:the parallelism number set does not cover the entire input")
  val init_cycles = 32 /** This is hardcoded for zuc 128 implementation as per specs of init-mode.*/
}

/** Interface definition of ZUC */
class zuc128IO(p: zucParams) extends Bundle {
  val in = Flipped(Decoupled(new Bundle() {
  val key = Vec(p.keys_num, UInt(8.W))
  val IV = Vec(p.keys_num, UInt(8.W))
  }))
  val KeyStream = Decoupled(SInt(p.KeyStream_wordsize.W))
  override def cloneType = (new zuc128IO(p)).asInstanceOf[this.type]
}

class zuc128(p:zucParams) extends  Module {

  val io = IO(new zuc128IO(p))

  /**The 16 Linear Feedback Shift Registers**/
  val LFSR_S: Vec[UInt] = Reg(Vec(16, UInt(p.KeyStream_wordsize.W)))
  /** the registers of the non linear function F **/
  val F_R: Vec[SInt] = Reg(Vec(2, SInt(p.KeyStream_wordsize.W)))
  /** the outputs of BitReorganization **/
  val BRC_X: Vec[SInt] = WireInit(VecInit(Seq.fill(4)(0.S(p.KeyStream_wordsize.W))))
  /** Obtaining S boxes from Scala implementation and mapping them for Chisel implementation**/
  val S0 = VecInit(zuc128_model.S0.map(_.U))
  val S1 = VecInit(zuc128_model.S1.map(_.U))
  val Ek_d = VecInit(zuc128_model.EK_d.map(_.U))
  val w: SInt = WireInit(0.S)

  /** LFSR with initialization mode **/
  def LFSRWithInitialisationMode(u: UInt) = {
    var f = LFSR_S(0)
    var v = zuc128.MulByPow2(LFSR_S(0), 8);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(4), 20);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(10), 21);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(13), 17);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(15), 15);
    f = zuc128.AddM(f, v);
    f = zuc128.AddM(f, u);
    /** update the state **/
    for (i <- 0 until 15) {
      LFSR_S(i) := LFSR_S(i + 1)
    }
    LFSR_S(15) := f
  }

  /** LFSR with work mode **/
  def LFSRWithWorkMode() = {
    var f = LFSR_S(0);
    var v = zuc128.MulByPow2(LFSR_S(0), 8);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(4), 20);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(10), 21);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(13), 17);
    f = zuc128.AddM(f, v);
    v = zuc128.MulByPow2(LFSR_S(15), 15);
    f = zuc128.AddM(f, v);
    /* update the state */
    for (i <- 0 until 15) {
      LFSR_S(i) := LFSR_S(i + 1);
    }
    LFSR_S(15) := f;
  }

  /** BitReorganization
   * It extracts 128 bits from the cells of the LFSR and forms 4 of 32-bit words,
   * where the first three words will be used by the nonlinear function F in the bottom layer,
   * and the last word will be involved in producing the keystream. **/
  def BitReorganization(): Unit = {
    BRC_X(0) := ((LFSR_S(15) & 0x7FFF8000.U).asSInt() << 1) | (LFSR_S(14) & 0xFFFF.U).asSInt();
    BRC_X(1) := ((LFSR_S(11) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(9) >> 15).asSInt()
    BRC_X(2) := ((LFSR_S(7) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(5) >> 15).asSInt()
    BRC_X(3) := ((LFSR_S(2) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(0) >> 15).asSInt()
  }

  /**The nonlinear function F has 2 of 32-bit memory cells R1 and R2**/
  def F(): SInt = {
    val  W1 = WireInit(0.S(32.W))
    val  W2 = WireInit(0.S(32.W))
    val u, v = WireInit(0.S(32.W))
    W1 := F_R(0) + BRC_X(1)
    W2 := F_R(1) ^ BRC_X(2)
    u := (zuc128.L1((W1 << 16).asUInt() (31,0) | (W2 >> 16).asUInt()))
    v := (zuc128.L2((W2 << 16).asUInt()(31,0)  | (W1 >> 16).asUInt()))
    F_R(0) := (zuc128.MAKEU32(S0((u >> 24).asUInt()), S1((u >> 16)(7,0)), S0((u >> 8)(7, 0)), S1(u (7, 0)))).asSInt()
    F_R(1) := (zuc128.MAKEU32(S0((v >> 24).asUInt()), S1((v >> 16)(7, 0)), S0((v >> 8)(7, 0)), S1(v (7, 0)))).asSInt()
    BRC_X(0) ^ (zuc128.MAKEU32(S0((u >> 24).asUInt()), S1((u >> 16)(7,0)), S0((u >> 8)(7, 0)), S1(u (7, 0)))).asSInt() +& (zuc128.MAKEU32(S0((v >> 24).asUInt()), S1((v >> 16)(7, 0)), S0((v >> 8)(7, 0)), S1(v (7, 0)))).asSInt()
    ((BRC_X(0) ^ F_R(0)) + F_R(1))
  }

  /** Starting FSM */
  val state = RegInit(zuc128.idle)
  io.in.ready := true.B
  io.KeyStream.noenq() //Initialize valid to false and bits to don't care until set to any other value

  switch(state) {
    is(zuc128.idle) {
      when(io.in.ready && io.in.valid) {
        state := zuc128.loadKey
      } .otherwise {
        state := zuc128.idle
      }
    }
    is(zuc128.loadKey) {
      val (count,done) = Counter(0 until p.keys_num by p.load_cycles,state === zuc128.loadKey)
      /**The key loading procedure will expand the initial key and the initial vector into 16 of 31-bit integers as the initial state of the LFSR.*/
      for (i <- 0 until p.load_cycles) {
        LFSR_S(count+ i.U) := zuc128.MAKEU31(io.in.bits.key(count + i.U), Ek_d(count + i.U), io.in.bits.IV(count + i.U))
       }

      /* set F_R1 and F_R2 to zero */
      for (i <- 0 until 2) {
        F_R(i) := 0.S
      }
      when(done){
        state := zuc128.initMode
      }.otherwise{
        state := zuc128.loadKey
      }
    }
    is(zuc128.initMode) {
      /**Then the cipher runs the following operations 32 times*/
      val nCount = RegInit(p.init_cycles.U)
      when(nCount > 0.U && state === zuc128.initMode) {

        BitReorganization()
        w := F()
        LFSRWithInitialisationMode((w >> 1).asUInt())
        nCount := nCount - 1.U
      }
      when(nCount === 0.U) {
        state := zuc128.workMode
      }.otherwise {
        state := zuc128.initMode
      }
    }
    is(zuc128.workMode) {
      /**After the initialization stage, the algorithm moves into the working stage.
       * At the working stage, the algorithm executes the following operations once, and discards the output W of F*/
      BitReorganization()
      F()
      LFSRWithWorkMode()
      state := zuc128.genKeystream
    }
    is(zuc128.genKeystream) {
      /**Then the algorithm goes into the stage of producing keystream,
       * i.e., for each iteration, the following operations are executed once, and a 32-bit word Z is produced as an output*/
      val i = RegInit(0.U)
      when(i < p.KStreamlen.U && state === zuc128.genKeystream) {
        BitReorganization();
        io.KeyStream.bits := F() ^ BRC_X(3)
        io.KeyStream.valid := true.B
        LFSRWithWorkMode()
        i := i + 1.U
      }
      when(i === p.KStreamlen.U) {
        state := zuc128.idle
      }.otherwise {
        state := zuc128.genKeystream
      }
    }
  }
}