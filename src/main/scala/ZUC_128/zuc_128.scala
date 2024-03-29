package ZUC_128

import chisel3._
import chisel3.util._

import scala.collection.mutable.ArrayBuffer


object zuc128_model {
  /**The 16 Linear Feedback Shift Registers*/
  var LFSR_S: ArrayBuffer[BigInt] = (new ArrayBuffer[BigInt]) ++ Seq.fill(16)(BigInt(0))
  /** the registers of the non linear function F */
    var F_R: ArrayBuffer[BigInt] = (new ArrayBuffer[BigInt]) ++ Seq.fill(2)(BigInt(0))
  /** the outputs of BitReorganization stage*/
  var BRC_X: ArrayBuffer[Int] = (new ArrayBuffer[Int]) ++ Seq.fill(4)(0)

  var w: Int = 0;
  /** the s-boxes */
  val S0: Seq[Int] = Seq(0x3e, 0x72, 0x5b, 0x47, 0xca, 0xe0, 0x00, 0x33, 0x04, 0xd1, 0x54, 0x98, 0x09, 0xb9, 0x6d, 0xcb, 0x7b, 0x1b, 0xf9, 0x32, 0xaf, 0x9d, 0x6a, 0xa5, 0xb8, 0x2d, 0xfc, 0x1d, 0x08, 0x53, 0x03, 0x90, 0x4d, 0x4e, 0x84, 0x99, 0xe4, 0xce, 0xd9, 0x91, 0xdd, 0xb6, 0x85, 0x48, 0x8b, 0x29, 0x6e, 0xac,
    0xcd, 0xc1, 0xf8, 0x1e, 0x73, 0x43, 0x69, 0xc6, 0xb5, 0xbd, 0xfd, 0x39, 0x63, 0x20, 0xd4, 0x38, 0x76, 0x7d, 0xb2, 0xa7, 0xcf, 0xed, 0x57, 0xc5, 0xf3, 0x2c, 0xbb, 0x14, 0x21, 0x06, 0x55, 0x9b,
    0xe3, 0xef, 0x5e, 0x31, 0x4f, 0x7f, 0x5a, 0xa4, 0x0d, 0x82, 0x51, 0x49, 0x5f, 0xba, 0x58, 0x1c, 0x4a, 0x16, 0xd5, 0x17, 0xa8, 0x92, 0x24, 0x1f, 0x8c, 0xff, 0xd8, 0xae, 0x2e, 0x01, 0xd3, 0xad,
    0x3b, 0x4b, 0xda, 0x46, 0xeb, 0xc9, 0xde, 0x9a, 0x8f, 0x87, 0xd7, 0x3a, 0x80, 0x6f, 0x2f, 0xc8, 0xb1, 0xb4, 0x37, 0xf7, 0x0a, 0x22, 0x13, 0x28, 0x7c, 0xcc, 0x3c, 0x89, 0xc7, 0xc3, 0x96, 0x56,
    0x07, 0xbf, 0x7e, 0xf0, 0x0b, 0x2b, 0x97, 0x52, 0x35, 0x41, 0x79, 0x61, 0xa6, 0x4c, 0x10, 0xfe, 0xbc, 0x26, 0x95, 0x88, 0x8a, 0xb0, 0xa3, 0xfb, 0xc0, 0x18, 0x94, 0xf2, 0xe1, 0xe5, 0xe9, 0x5d,
    0xd0, 0xdc, 0x11, 0x66, 0x64, 0x5c, 0xec, 0x59, 0x42, 0x75, 0x12, 0xf5, 0x74, 0x9c, 0xaa, 0x23, 0x0e, 0x86, 0xab, 0xbe, 0x2a, 0x02, 0xe7, 0x67, 0xe6, 0x44, 0xa2, 0x6c, 0xc2, 0x93, 0x9f, 0xf1,
    0xf6, 0xfa, 0x36, 0xd2, 0x50, 0x68, 0x9e, 0x62, 0x71, 0x15, 0x3d, 0xd6, 0x40, 0xc4, 0xe2, 0x0f, 0x8e, 0x83, 0x77, 0x6b, 0x25, 0x05, 0x3f, 0x0c, 0x30, 0xea, 0x70, 0xb7, 0xa1, 0xe8, 0xa9, 0x65,
    0x8d, 0x27, 0x1a, 0xdb, 0x81, 0xb3, 0xa0, 0xf4, 0x45, 0x7a, 0x19, 0xdf, 0xee, 0x78, 0x34, 0x60)

  val S1: Seq[Int] = Seq(0x55, 0xc2, 0x63, 0x71, 0x3b, 0xc8, 0x47, 0x86, 0x9f, 0x3c, 0xda, 0x5b, 0x29, 0xaa, 0xfd, 0x77,
    0x8c, 0xc5, 0x94, 0x0c, 0xa6, 0x1a, 0x13, 0x00, 0xe3, 0xa8, 0x16, 0x72, 0x40, 0xf9, 0xf8, 0x42,
    0x44, 0x26, 0x68, 0x96, 0x81, 0xd9, 0x45, 0x3e, 0x10, 0x76, 0xc6, 0xa7, 0x8b, 0x39, 0x43, 0xe1,
    0x3a, 0xb5, 0x56, 0x2a, 0xc0, 0x6d, 0xb3, 0x05, 0x22, 0x66, 0xbf, 0xdc, 0x0b, 0xfa, 0x62, 0x48,
    0xdd, 0x20, 0x11, 0x06, 0x36, 0xc9, 0xc1, 0xcf, 0xf6, 0x27, 0x52, 0xbb, 0x69, 0xf5, 0xd4, 0x87,
    0x7f, 0x84, 0x4c, 0xd2, 0x9c, 0x57, 0xa4, 0xbc, 0x4f, 0x9a, 0xdf, 0xfe, 0xd6, 0x8d, 0x7a, 0xeb,
    0x2b, 0x53, 0xd8, 0x5c, 0xa1, 0x14, 0x17, 0xfb, 0x23, 0xd5, 0x7d, 0x30, 0x67, 0x73, 0x08, 0x09,
    0xee, 0xb7, 0x70, 0x3f, 0x61, 0xb2, 0x19, 0x8e, 0x4e, 0xe5, 0x4b, 0x93, 0x8f, 0x5d, 0xdb, 0xa9,
    0xad, 0xf1, 0xae, 0x2e, 0xcb, 0x0d, 0xfc, 0xf4, 0x2d, 0x46, 0x6e, 0x1d, 0x97, 0xe8, 0xd1, 0xe9,
    0x4d, 0x37, 0xa5, 0x75, 0x5e, 0x83, 0x9e, 0xab, 0x82, 0x9d, 0xb9, 0x1c, 0xe0, 0xcd, 0x49, 0x89,
    0x01, 0xb6, 0xbd, 0x58, 0x24, 0xa2, 0x5f, 0x38, 0x78, 0x99, 0x15, 0x90, 0x50, 0xb8, 0x95, 0xe4,
    0xd0, 0x91, 0xc7, 0xce, 0xed, 0x0f, 0xb4, 0x6f, 0xa0, 0xcc, 0xf0, 0x02, 0x4a, 0x79, 0xc3, 0xde,
    0xa3, 0xef, 0xea, 0x51, 0xe6, 0x6b, 0x18, 0xec, 0x1b, 0x2c, 0x80, 0xf7, 0x74, 0xe7, 0xff, 0x21,
    0x5a, 0x6a, 0x54, 0x1e, 0x41, 0x31, 0x92, 0x35, 0xc4, 0x33, 0x07, 0x0a, 0xba, 0x7e, 0x0e, 0x34,
    0x88, 0xb1, 0x98, 0x7c, 0xf3, 0x3d, 0x60, 0x6c, 0x7b, 0xca, 0xd3, 0x1f, 0x32, 0x65, 0x04, 0x28,
    0x64, 0xbe, 0x85, 0x9b, 0x2f, 0x59, 0x8a, 0xd7, 0xb0, 0x25, 0xac, 0xaf, 0x12, 0x03, 0xe2, 0xf2)

  /** Constant used with key and IV for initializing LSFR*/
  val EK_d: Seq[BigInt] = Seq(0x44D7, 0x26BC, 0x626B, 0x135E, 0x5789, 0x35E2, 0x7135, 0x09AF,
    0x4D78, 0x2F13, 0x6BC4, 0x1AF1, 0x5E26, 0x3C4D, 0x789A, 0x47AC)

  val MASK = 0x7FFFFFFF

  /** c = a + b mod (2^31 – 1) */
  def AddM(a: BigInt, b: Int): BigInt = {
    var c = a + b
    (c & MASK) + (c >> 31)
  }

  def MulByPow2(x: BigInt, k: Int): Int = {
    ((x.toInt << k) | (x.toInt >>> (31 - k))) & MASK
  }

  /** LFSR with initialization mode */
  def LFSRWithInitialisationMode(u: Int) = {
    var f: BigInt = BigInt(0)
    var v: Int = 0
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
      LFSR_S(i) = LFSR_S(i + 1);
    }
    LFSR_S(15) = f
  }

  /** LFSR with work mode */
  def LFSRWithWorkMode() = {
    var f: BigInt = BigInt(0)
    var v: Int = 0
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
      LFSR_S(i) = LFSR_S(i + 1);
    }
    LFSR_S(15) = f;
  }

  /** BitReorganization
  * It extracts 128 bits from the cells of the LFSR and forms 4 of 32-bit words,
  * where the first three words will be used by the nonlinear function F in the bottom layer,
  * and the last word will be involved in producing the keystream. */
  def BitReorganization() = {

    BRC_X(0) = (((LFSR_S(15) & 0x7FFF8000) << 1) | (LFSR_S(14) & 0xFFFF)).toInt;
    BRC_X(1) = (((LFSR_S(11) & 0xFFFF) << 16) | (LFSR_S(9) >> 15)).toInt;
    BRC_X(2) = (((LFSR_S(7) & 0xFFFF) << 16) | (LFSR_S(5) >> 15)).toInt;
    BRC_X(3) = (((LFSR_S(2) & 0xFFFF) << 16) | (LFSR_S(0) >> 15)).toInt;
  }

  /** Function used to calculate L1 and L2 */
  def ROT(a: Int, k: Int): Int = {
    (a << k) | (a >>> (32 - k))
  }
  /** Both L1 and L2 are linear transforms from 32-bit words to 32-bit words */
  def L1(x: Int): Int = {
    x ^ ROT(x, 2) ^ ROT(x, 10) ^ ROT(x, 18) ^ ROT(x, 24);
  }
  def L2(X: Int): Int = {
    X ^ ROT(X, 8) ^ ROT(X, 14) ^ ROT(X, 22) ^ ROT(X, 30)
  }

  /** Function used in F to calculate F_R1 and F_R2 */
  def MAKEU32(a: Int, b: Int, c: Int, d: Int): BigInt = {
    (a << 24) | (b << 16) | (c << 8) | (d)
  }
  /**The nonlinear function F has 2 of 32-bit memory cells R1 and R2*/
  def F():Int= {
    var W, W1, W2: Int = 0
    var  u, v: Int = 0
    val MASK1 = 0xFF
    W = ((BRC_X(0) ^ F_R(0)) + F_R(1)).toInt
    W1 = (F_R(0) + BRC_X(1)).toInt
    W2 = (F_R(1) ^ BRC_X(2)).toInt
    u = (L1((W1 << 16) | (W2 >>> 16)))
    v = (L2((W2 << 16) | (W1 >>> 16)))
    F_R(0) = MAKEU32(S0(u >>> 24).toInt, S1((u >>> 16) & MASK1), S0((u >>> 8) & MASK1), S1(u & MASK1))
    F_R(1) = MAKEU32(S0(v >>> 24), S1((v >>> 16) & MASK1), S0((v >>> 8) & MASK1), S1(v & MASK1))
    W
  }

  /** To calculate pre-initialized LSFR values from key, Ek_d and IV */
  def MAKEU31(a: Int, b: BigInt, c: Int):BigInt = {
    (a << 23) | (b << 8) | c
  }

  /** expand LFSR key in initial mode*/
  def init_LFSR_key_exp (k: Seq[Int] , iv: Seq[Int]): ArrayBuffer[BigInt] = {
  for (i <- 0 until 16) {
    LFSR_S(i) = MAKEU31(k(i), EK_d(i), iv(i));
  }
    LFSR_S
  }

  /** initialization stage*/
  def Initialization(k: Seq[Int] , iv: Seq[Int]) = {

    var nCount: Int = 0
    /** The key loading procedure will expand the initial key and the initial vector into
       16 of 31-bit integers as the initial state of the LFSR. */
    init_LFSR_key_exp(k, iv)
    /** set F_R1 and F_R2 to zero */
    for (i <- 0 until 2) {
      F_R(i) = 0
    }
    nCount = 32
    while (nCount > 0)
    {
      BitReorganization()
      w = F()
      LFSRWithInitialisationMode(w >>> 1)
      nCount = nCount -1
    }
  }

  /** After the initialization stage, the algorithm moves into the working stage.
  * Then the algorithm goes into the stage of producing keystream,
  * i.e., for each iteration, the following operations are executed once, and a 32-bit word Z is produced as an output*/
  def GenerateKeystream(KeystreamLen: Int): ArrayBuffer[Int] = {
    var pKeystream: ArrayBuffer[Int] = new ArrayBuffer[Int]() ++ Seq.fill(KeystreamLen)(0)
    var i: Int = 0;
    {
      BitReorganization();
      F(); /** discard the output of F */
      LFSRWithWorkMode()
    }
    for (i <- 0 until KeystreamLen) {
      BitReorganization();
      pKeystream(i) = F() ^ BRC_X(3);
      LFSRWithWorkMode();
    }
    pKeystream
  }

}