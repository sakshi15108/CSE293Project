package ZUC_128

import chisel3._
import chisel3.util._

import scala.collection.mutable.ArrayBuffer

case class zucParams(KSlen: Int) {
  val LFSR_wordSize = 32
  val keys_num = 16
  val KeyLen = 8
  val KStreamlen = KSlen
}

/*object  zuc128 {
 // val p: zucParams;
  val LFSR_wordSize = 32
  val KeyLen = 128
  val KStreamlen = 1
  /*LSFR state registers*/
  val LFSR_S: Vec[UInt] = Reg(Vec(16, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(16)(BigInt(0))
  /* the registers of F */
  val F_R: Vec[UInt] = Reg(Vec(2, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(2)(BigInt(0))
  /* the outputs of BitReorganization */
  val BRC_X: Vec[UInt] = Reg(Vec(4, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[Int]) ++ Seq.fill(4)(0)
//  val pKeystream : Vec[UInt] = Reg(Vec(KStreamlen, UInt(LFSR_wordSize.W))) //: ArrayBuffer[BigInt] = new ArrayBuffer[BigInt]() ++ Seq.fill(p.KStreamlen)(BigInt(0))

  val S0 = zuc128_model.S0.map(_.U)
  val S1 = zuc128_model.S1.map(_.U)
  val w = RegInit(0.U);

  def AddM(a: UInt, b: UInt): UInt = {
    var c = a + b
    (c(30,0)) +& (c >> 31).asUInt()
  }

  def MulByPow2(x: UInt, k: Int): UInt = {
    ((x << k) | (x >> (31 - k))(30,0))
  }

  def LFSRWithInitialisationMode(u: UInt) = {
    var f: UInt = 0.U
    var v: UInt = 0.U
    f = LFSR_S(0)
    v = MulByPow2(LFSR_S(0), 8);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(4), 20);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(10), 21);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(13), 17);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(15), 15);
    f = AddM(f, v);
    f = AddM(f, u);

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
    v = MulByPow2(LFSR_S(0), 8);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(4), 20);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(10), 21);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(13), 17);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(15), 15);
    f = AddM(f, v);

    /* update the state */
    for (i <- 0 until 15) {
      LFSR_S(i) := LFSR_S(i + 1);
    }
    LFSR_S(15) := f;
  }

  /* BitReorganization */
  def BitReorganization() = {
    BRC_X(0) := ((LFSR_S(15) & 0x7FFF8000.U) << 1) | (LFSR_S(14) & 0xFFFF.U);
    BRC_X(1) := (((LFSR_S(11) & 0xFFFF.U) << 16) | (LFSR_S(9) >> 15));
    BRC_X(2) := (((LFSR_S(7) & 0xFFFF.U) << 16) | (LFSR_S(5) >> 15));
    BRC_X(3) := (((LFSR_S(2) & 0xFFFF.U) << 16) | (LFSR_S(0) >> 15));
    //    println("in bit reorganization:\n")
    //    println(s" BRC_X3: ${BRC_X(3)}\n");
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

  def F():UInt= {
    var W, W1, W2: UInt = 0.U
    var  u, v: Int = 0
    val MASK1 = 0xFF
    W = ((BRC_X(0) ^ F_R(0)) + F_R(1))
    W1 = (F_R(0) + BRC_X(1))
    W2 = (F_R(1) ^ BRC_X(2))
    u = (L1((W1 << 16) | (W2 >> 16))).litValue().toInt
    v = (L2((W2 << 16) | (W1 >> 16))).litValue().toInt
    F_R(0) := MAKEU32(S0((u >>> 24)),S1((u >>> 16) & MASK1), S0((u >>> 8) & MASK1), S1(u & MASK1))
    F_R(1) := MAKEU32(S0(v >>> 24), S1((v >>> 16) & MASK1), S0((v >>> 8) & MASK1), S1(v & MASK1))
    //    println(p" value of F_R0 ${F_R(0)}")
    //    println(p" value of F_R1 ${F_R(1)}")
    W
  }

  def MAKEU31(a: UInt, b: UInt, c: UInt):UInt = {
    ((a << 23) | (b << 8) | c).asUInt()
  }

}
*/
class zuc128IO(p: zucParams) extends Bundle {
//val key = Flipped(Decoupled(UInt((p.KeyLen).W))) //FIXME
//val in = Flipped(Decoupled(new Bundle {
//  val aBlock = Vec(p.aElementsPerTransfer, SInt(p.w))
//  val bBlock = Vec(p.bElementsPerTransfer, SInt(p.w))
//}))
  val in = Input(new Bundle() {
  val key = Vec(p.keys_num, UInt((p.KeyLen).W))
  val IV = Vec(p.keys_num, UInt((p.KeyLen).W))
  })

  val KeyStream = Output(UInt(p.LFSR_wordSize.W))

  override def cloneType = (new zuc128IO(p)).asInstanceOf[this.type]
}

class zuc128(p:zucParams) extends  Module {

  // val p: zucParams;
  val LFSR_wordSize = 32
  val KeyLen = 128
  val KStreamlen = 1
  /*LSFR state registers*/
  val LFSR_S: Vec[UInt] = Reg(Vec(16, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(16)(BigInt(0))
  /* the registers of F */
  val F_R: Vec[UInt] = Reg(Vec(2, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[BigInt]) ++ Seq.fill(2)(BigInt(0))
  /* the outputs of BitReorganization */
  val BRC_X: Vec[UInt] = Reg(Vec(4, UInt(LFSR_wordSize.W))) //= (new ArrayBuffer[Int]) ++ Seq.fill(4)(0)
  //  val pKeystream : Vec[UInt] = Reg(Vec(KStreamlen, UInt(LFSR_wordSize.W))) //: ArrayBuffer[BigInt] = new ArrayBuffer[BigInt]() ++ Seq.fill(p.KStreamlen)(BigInt(0))

  val S0 = VecInit(zuc128_model.S0.map(_.U))
  val S1 = VecInit(zuc128_model.S1.map(_.U))
  val w = RegInit(0.U);

  def AddM(a: UInt, b: UInt): UInt = {
    var c = a + b
    (c(30,0)) +& (c >> 31).asUInt()
  }

  def MulByPow2(x: UInt, k: Int): UInt = {
    ((x << k) | (x >> (31 - k))) & zuc128_model.MASK.asUInt()
  }

  def LFSRWithInitialisationMode(u: UInt) = {
    var f: UInt = 0.U
    var v: UInt = 0.U
    f = LFSR_S(0)
    v = MulByPow2(LFSR_S(0), 8);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(4), 20);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(10), 21);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(13), 17);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(15), 15);
    f = AddM(f, v);
    f = AddM(f, u);

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
    v = MulByPow2(LFSR_S(0), 8);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(4), 20);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(10), 21);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(13), 17);
    f = AddM(f, v);
    v = MulByPow2(LFSR_S(15), 15);
    f = AddM(f, v);

    /* update the state */
    for (i <- 0 until 15) {
      LFSR_S(i) := LFSR_S(i + 1);
    }
    LFSR_S(15) := f;
  }

  /* BitReorganization */
  def BitReorganization() = {
    BRC_X(0) := ((LFSR_S(15) & 0x7FFF8000.U) << 1) | (LFSR_S(14) & 0xFFFF.U);
    BRC_X(1) := (((LFSR_S(11) & 0xFFFF.U) << 16) | (LFSR_S(9) >> 15));
    BRC_X(2) := (((LFSR_S(7) & 0xFFFF.U) << 16) | (LFSR_S(5) >> 15));
    BRC_X(3) := (((LFSR_S(2) & 0xFFFF.U) << 16) | (LFSR_S(0) >> 15));
    //    println("in bit reorganization:\n")
    //    println(s" BRC_X3: ${BRC_X(3)}\n");
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

  def F():UInt= {
    var W, W1, W2: UInt = 0.U
    var  u, v: UInt = 0.U
    val MASK1 = 0xFF.U
    W = ((BRC_X(0) ^ F_R(0)) + F_R(1))
    W1 = (F_R(0) + BRC_X(1))
    W2 = (F_R(1) ^ BRC_X(2))
    u = (L1((W1 << 16) | (W2 >> 16)))//.litValue().toInt
    v = (L2((W2 << 16) | (W1 >> 16)))//.litValue().toInt
    F_R(0) := MAKEU32(S0(u >> 24), S1((u >> 16) & MASK1), S0((u >> 8) & MASK1), S1(u & MASK1))
    F_R(1) := MAKEU32(S0(v >> 24), S1((v >> 16) & MASK1), S0((v >> 8) & MASK1), S1(v & MASK1))
    //    println(p" value of F_R0 ${F_R(0)}")
    //    println(p" value of F_R1 ${F_R(1)}")
    W
  }

  def MAKEU31(a: UInt, b: UInt, c: UInt):UInt = {
    ((a << 23) | (b << 8) | c).asUInt()
  }


/////////////////////////////////////////////////////////////////CLASS part:
  val io = IO(new zuc128IO(p))

  /* initialize */
  /* expand key */
  val started = RegInit(false.B)
  when(!started) {
    for (i <- 0 until 16) {
      LFSR_S(i) := MAKEU31(io.in.key(i), zuc128_model.EK_d(i).asUInt(), io.in.IV(i));
    }
    /* set F_R1 and F_R2 to zero */
    for (i <- 0 until 2) {
      F_R(i) := 0.U
    }
    started := true.B
  }


  val nCount = RegInit(32.U)
  val InitFIN = RegInit(false.B)
  when (nCount > 0.U && started)
  { BitReorganization()
    w := F()
    LFSRWithInitialisationMode((w >> 1).asUInt())
    printf(p"During IS @${nCount}: BRC_X0: ${BRC_X(0)},BRC_X1: ${BRC_X(1)},BRCX_2: ${BRC_X(2)}, BRC_X3: ${BRC_X(3)},FR0: ${F_R(0)},FR1: ${F_R(1)}\n");
    nCount := nCount -1.U
  } .otherwise {
    InitFIN := true.B
    //    nCount := 32.U
  }
  printf(p"During IS @${nCount}: BRC_X0: ${BRC_X(0)},BRC_X1: ${BRC_X(1)},BRCX_2: ${BRC_X(2)}, BRC_X3: ${BRC_X(3)},FR0: ${F_R(0)},FR1: ${F_R(1)}\n");

  //Generate keystream:
  val Wdiscarded = RegInit(false.B)
  when( InitFIN) {
    BitReorganization();
    F(); /* discard the output of F */
    LFSRWithWorkMode()
    Wdiscarded := true.B
  }

  //  val (KS_len_i, KS_end) = Counter(0 until p.KStreamlen, Wdiscarded)
  val i = RegInit(0.U)
  when (i < p.KStreamlen.U && Wdiscarded) {//<- 0 until p.KStreamlen) {
    BitReorganization();
    io.KeyStream :=  F() ^  BRC_X(3);
    printf(p"Output value at clock ${i} is ${io.KeyStream}\n")
    //    println(s"After WS: BRC_X0: ${zuc128.BRC_X(0)},BRC_X1: ${zuc128.BRC_X(1)},BRCX_2: ${zuc128.BRC_X(2)}, BRC_X3: ${zuc128.BRC_X(3)},FR0: ${zuc128.F_R(0)},FR1: ${zuc128.F_R(1)}\n");
    LFSRWithWorkMode();
    //    io.KeyStream := zuc128.pKeystream(i)
    i := i + 1.U
  } .otherwise {
    i := 0.U
    io.KeyStream := 0.U
  }
 /* val IS_count = RegInit(0.U)
  when(IS_count <= 1.U) {
    for (i <- 0 until 16) {
      zuc128.LFSR_S(i) := zuc128.MAKEU31(io.in.key(i), zuc128_model.EK_d(i).asUInt(), io.in.IV(i));
    }
    /* set F_R1 and F_R2 to zero */
    for (i <- 0 until 2) {
      zuc128.F_R(i) := 0.U
    }
    IS_count := IS_count + 1.U
  } .otherwise {
    IS_count := 0.U
  }

  val nCount = RegInit(32.U)
  when (nCount > 0.U)
  { zuc128.BitReorganization()
    zuc128.w := zuc128.F()
    zuc128.LFSRWithInitialisationMode((zuc128.w >> 1).asUInt())
    println(s"During IS @${nCount}: BRC_X0: ${zuc128.BRC_X(0)},BRC_X1: ${zuc128.BRC_X(1)},BRCX_2: ${zuc128.BRC_X(2)}, BRC_X3: ${zuc128.BRC_X(3)},FR0: ${zuc128.F_R(0)},FR1: ${zuc128.F_R(1)}\n");
    nCount := nCount -1.U
  } .otherwise {
    nCount := 32.U
  }
  println(s"During IS @${nCount}: BRC_X0: ${zuc128.BRC_X(0)},BRC_X1: ${zuc128.BRC_X(1)},BRCX_2: ${zuc128.BRC_X(2)}, BRC_X3: ${zuc128.BRC_X(3)},FR0: ${zuc128.F_R(0)},FR1: ${zuc128.F_R(1)}\n");

  //Generate keystream:
  val WS_count = RegInit(0.U)
  val Wdiscarded = RegInit(0.U)
  when(WS_count <= 1.U) {
    zuc128.BitReorganization();
    zuc128.F(); /* discard the output of F */
    zuc128.LFSRWithWorkMode()
    WS_count := WS_count + 1.U
    Wdiscarded := 1.U
  } .otherwise {
    WS_count := 0.U
  }

//  val (KS_len_i, KS_end) = Counter(0 until p.KStreamlen, Wdiscarded)
  val i = RegInit(0.U)
  when (i < p.KStreamlen.U) {//<- 0 until p.KStreamlen) {
    zuc128.BitReorganization();
    io.KeyStream :=  zuc128.F() ^  zuc128.BRC_X(3);
    println(p"Output value at clock ${i} is ${io.KeyStream}\n")
//    println(s"After WS: BRC_X0: ${zuc128.BRC_X(0)},BRC_X1: ${zuc128.BRC_X(1)},BRCX_2: ${zuc128.BRC_X(2)}, BRC_X3: ${zuc128.BRC_X(3)},FR0: ${zuc128.F_R(0)},FR1: ${zuc128.F_R(1)}\n");
    zuc128.LFSRWithWorkMode();
//    io.KeyStream := zuc128.pKeystream(i)
    i := i + 1.U
  } .otherwise {
    i := 0.U
  }
*/

}