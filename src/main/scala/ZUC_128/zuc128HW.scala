package ZUC_128

import chisel3._
import chisel3.util._

import scala.collection.mutable.ArrayBuffer


object  zuc128 {

  val idle :: loadKey :: initMode :: workMode :: genKeystream :: Nil = Enum(5)

  def AddM(a: UInt, b: UInt): UInt = {
    var c = a + b
    (c(30,0)) +& (c >> 31).asUInt()
  }
  def MulByPow2(x: UInt, k: Int): UInt = {
    ((x << k) | (x >> (31 - k))) & zuc128_model.MASK.asUInt()
  }
  def ROT(a: UInt, k: Int): UInt = {
    (a << k) | (a >> (32 - k))
  }
  def L1(x: UInt): UInt = {
    x ^ ROT(x, 2) ^ ROT(x, 10) ^ ROT(x, 18) ^ ROT(x, 24);
  }
  def L2(X: UInt): UInt = {
    X ^ ROT(X, 8) ^ ROT(X, 14) ^ ROT(X, 22) ^ ROT(X, 30)
  }
  def MAKEU32(a:UInt, b: UInt, c: UInt, d: UInt): UInt = {
    ((a << 24) | (b << 16) | (c << 8) | (d))
  }
  def MAKEU31(a: UInt, b: UInt, c: UInt):UInt = {
    ((a << 23) | (b << 8) | c).asUInt()
  }
}

case class zucParams(KSlen: Int) {
  val LFSR_wordSize = 32
  val keys_num = 16
  val KeyLen = 8
  val KStreamlen = KSlen
}

class zuc128IO(p: zucParams) extends Bundle {
  val in = Input(new Bundle() {
  val key = Vec(p.keys_num, UInt((p.KeyLen).W))
  val IV = Vec(p.keys_num, UInt((p.KeyLen).W))
  })
  val KeyStream = Output(UInt(p.LFSR_wordSize.W))
  override def cloneType = (new zuc128IO(p)).asInstanceOf[this.type]
}

class zuc128(p:zucParams) extends  Module {

  val io = IO(new zuc128IO(p))

  /*LSFR state registers*/
  val LFSR_S: Vec[UInt] = Reg(Vec(16, UInt(p.LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(16)(BigInt(0))
  /* the registers of F */
  val F_R: Vec[UInt] = Reg(Vec(2, UInt(p.LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(2)(BigInt(0))
  /* the outputs of BitReorganization */
  val BRC_X: Vec[SInt] = Reg(Vec(4, SInt(p.LFSR_wordSize.W))) //= (new ArrayBuffer[Int]) ++ Seq.fill(4)(0)
  val S0 = VecInit(zuc128_model.S0.map(_.U))
  val S1 = VecInit(zuc128_model.S1.map(_.U))
  val w = RegInit(0.U);

  def LFSRWithInitialisationMode(u: UInt) = {
    var f: UInt = 0.U
    var v: UInt = 0.U
    f = LFSR_S(0)
    v = zuc128.MulByPow2(LFSR_S(0), 8);
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
    /* update the state */
    for (i <- 0 until 15) {
      LFSR_S(i) := LFSR_S(i + 1)
    }
    LFSR_S(15) := f
  }

  /* LFSR with work mode */
  def LFSRWithWorkMode() = {
    var f: UInt = 0.U
    var v: UInt = 0.U
    f = LFSR_S(0);
    v = zuc128.MulByPow2(LFSR_S(0), 8);
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

  /* BitReorganization */
  def BitReorganization(): Unit = {
    BRC_X(0) := ((LFSR_S(15) & 0x7FFF8000.U).asSInt() << 1) | (LFSR_S(14) & 0xFFFF.U).asSInt();
    BRC_X(1) := ((LFSR_S(11) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(9) >> 15).asSInt()
    BRC_X(2) := ((LFSR_S(7) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(5) >> 15).asSInt()
    BRC_X(3) := ((LFSR_S(2) & 0xFFFF.U).asSInt() << 16) | (LFSR_S(0) >> 15).asSInt()
  }

  def F(): UInt = {
    var  W1 = 0.S
    var  W2 = 0.U
    var u, v = 0.U
    val MASK1 = 0xFF.U
    printf(p"Priting w @starting: ${((BRC_X(0).asUInt() ^ F_R(0)) + F_R(1))}\n")
    printf(p"FR0: ${F_R(0)}, FR0 asSInt: ${F_R(0).asSInt()}, BRCX1: ${BRC_X(1)}\n")
    W1 = F_R(0).asSInt() + BRC_X(1)
    printf(p"W1: ${W1}\n")
    W2 = F_R(1) ^ BRC_X(2).asUInt()
    u = (zuc128.L1((W1.asUInt() << 16) | (W2 >> 16)))
    v = (zuc128.L2((W2 << 16) | (W1.asUInt() >> 16)))
    F_R(0) := zuc128.MAKEU32(S0((u >> 24).asUInt()), S1((u >> 16)(7,0)), S0((u >> 8)(7, 0)), S1(u (7, 0)))
    F_R(1) := zuc128.MAKEU32(S0((v >> 24).asUInt()), S1((v >> 16)(7, 0)), S0((v >> 8)(7, 0)), S1(v (7, 0)))
    printf(p"Priting w @END: ${((BRC_X(0).asUInt() ^ F_R(0)) + F_R(1))}\n")
    ((BRC_X(0).asUInt() ^ F_R(0)) + F_R(1))
  }

  val state = RegInit(zuc128.idle)
  io.KeyStream := 0.U
  switch(state) {
    is(zuc128.idle) {
      state := zuc128.loadKey
    }
    is(zuc128.loadKey) {
      for (i <- 0 until 16) {
        LFSR_S(i) := zuc128.MAKEU31(io.in.key(i), zuc128_model.EK_d(i).asUInt(), io.in.IV(i));
      }
      /* set F_R1 and F_R2 to zero */
      for (i <- 0 until 2) {
        F_R(i) := 0.U
      }
      state := zuc128.initMode
      printf(p" AT state initmode created initial LFSR and set R0 and R1 to zero \n")
    }
    is(zuc128.initMode) {
      val nCount = RegInit(32.U)
      when(nCount > 0.U && state === zuc128.initMode) {
//        for (i <- 0 until 16) {
//          printf(p" LFSR ${LFSR_S(i)}\n")
//        }
        BitReorganization()
//        printf(p"@${nCount}: BRC_X0:${BRC_X(0)}  BRC_X1:${BRC_X(1)}  BRC_X2:${BRC_X(2)}  BRC_X3:${BRC_X(3)}\n")
        printf(p"@${nCount}\n")
        w := F()
        LFSRWithInitialisationMode((w >> 1).asUInt())
//        printf(p"FR0:${F_R(0)}  FR1:${F_R(1)}\n");
//        printf(p"W: ${w}\n")
        nCount := nCount - 1.U
      }
      when(nCount === 0.U) {
        state := zuc128.workMode
      }.otherwise {
        state := zuc128.initMode
      }
    }
    is(zuc128.workMode) {
      printf(p"At workmode \n ")
      BitReorganization()
      F()
      LFSRWithWorkMode()
      state := zuc128.genKeystream
    }
    is(zuc128.genKeystream) {
      printf(p"At genKeystream")
      val i = RegInit(0.U)
      when(i < p.KStreamlen.U && state === zuc128.genKeystream) {
        BitReorganization();
        io.KeyStream := F() ^ BRC_X(3).asUInt();
        printf(p"Output value at clock ${i} is ${io.KeyStream}\n")
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