package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer

object zuc128_ScalaModelTestData1{
  val key : Seq[Int] = Seq(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 ,0x00,0x00)
  val IV : Seq[Int]= Seq(0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00 ,0x00)

  /**LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x0044d700, 0x0026bc00, 0x00626b00, 0x00135e00, 0x00578900,
    0x0035e200, 0x00713500, 0x0009af00, 0x004d7800, 0x002f1300, 0x006bc400, 0x001af100, 0x005e2600,
    0x003c4d00, 0x00789a00, 0x0047ac00)

  /**LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x7ce15b8b, 0x747ca0c4, 0x6259dd0b, 0x47a94c2b, 0x3a89c82e,
    0x32b433fc, 0x231ea13f, 0x31711e42,0x4ccce955, 0x3fb6071e, 0x161d3512, 0x7114b136, 0x5154d452,
    0x78c69a74, 0x4f26ba6b, 0x3e1b8d6a)

  /** F_R1 and F_R2 values after Initialization mode */
  val R1_post_init = 0x14cfd44c
  val R2_post_init = 0x8c6de800

  /** W value after Initialization mode */
  val W_post_init = 0x1b85d1e6
  /**Key Stream Length*/
  val KSlen : Int = 32
  /**Generated Key stream*/
  val Z_post_gen: Seq[Int] = Seq(0x27bede74, 0x18082da, 0x87d4e5b6, 0x9f18bf66, 0x32070e0f, 0x39b7b692, 0xb4673edc, 0x3184a48e, 0x27636f44, 0x14510d62, 0xcc15cfe1, 0x94ec4f6d, 0x4b8c8fcc, 0x630648ba, 0xdf41b6f9, 0xd16a36ca, 0x203ab30d, 0x2927857, 0x9e42af60, 0x74b9a8f8, 0x90115871, 0x99d29d46, 0xb4c4f1ec, 0x992995a7, 0xae2957bc, 0x7ea29792, 0x11157fcc, 0xf0966d98, 0xb0d2804b, 0xd039ffef, 0x98c34576, 0x92eb83c1)

}

object zuc128_ScalaModelTestData2{
  val key : Seq[Int] = Seq(0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff ,0xff,0xff)
  val IV : Seq[Int]= Seq(0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff)

  /**LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x7fc4d7ff, 0x7fa6bcff, 0x7fe26bff, 0x7f935eff, 0x7fd789ff, 0x7fb5e2ff,
    0x7ff135ff, 0x7f89afff, 0x7fcd78ff, 0x7faf13ff, 0x7febc4ff, 0x7f9af1ff, 0x7fde26ff, 0x7fbc4dff,
    0x7ff89aff, 0x7fc7acff)

  /**LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x09a339ad, 0x1291d190, 0x25554227, 0x36c09187, 0x0697773b,
    0x443cf9cd, 0x6a4cd899, 0x49e34bd0,0x56130b14, 0x20e8f24c, 0x7a5b1dcc, 0x0c3cc2d1, 0x1cc082c8,
    0x7f5904a2, 0x55b61ce8, 0x1fe46106)

  /** F_R1 and F_R2 values after Initialization mode */
  val R1_post_init = 0xb8017bd5
  val R2_post_init = 0x9ce2de5c

  /** W value after Initialization mode */
  val W_post_init = 0xfce125a7
  /**Key Stream Length*/
  val KSlen : Int = 32
  /**Generated Key stream*/
  val Z_post_gen: Seq[Int] = Seq(0x657cfa0, 0x7096398b, 0x734b6cb4, 0x883eedf4, 0x257a76eb, 0x97595208, 0xd884adcd, 0xb1cbffb8, 0xe0f9d158, 0x46a0eed0, 0x15328503, 0x351138f7, 0x40d079af, 0x17296c23, 0x2c4f022d, 0x6e4acac6, 0x141dd74b, 0xddc63109, 0x4bd62e3a, 0x8baa567, 0x2d3b3436, 0x6811928, 0x3dad614c, 0x8df6d345, 0x2fe18442, 0x69592ad, 0xf926c8c1, 0x95f32662, 0xa1c69621, 0xa066492b, 0xddb70940, 0x360efedc)
}

object zuc128_ScalaModelTestData3 {

  val key : Seq[Int] = Seq(0x3d, 0x4c, 0x4b, 0xe9, 0x6a, 0x82, 0xfd, 0xae, 0xb5, 0x8f, 0x64, 0x1d, 0xb1, 0x7b ,0x45,0x5b)
  val IV : Seq[Int]= Seq(0x84 ,0x31 ,0x9a ,0xa8 ,0xde ,0x69 ,0x15 ,0xca ,0x1f ,0x6b ,0xda ,0x6b ,0xfb ,0xd8 ,0xc7 ,0x66)
  /**LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x1ec4d784, 0x2626bc31, 0x25e26b9a, 0x74935ea8, 0x355789de, 0x4135e269, 0x7ef13515, 0x5709afca,
    0x5acd781f, 0x47af136b, 0x326bc4da, 0x0e9af16b, 0x58de26fb, 0x3dbc4dd8, 0x22f89ac7, 0x2dc7ac66)
  /**LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x10da5941, 0x5b6acbf6, 0x17060ce1, 0x35368174, 0x5cf4385a, 0x479943df, 0x2753bab2, 0x73775d6a,
    0x43930a37, 0x77b4af31, 0x15b2e89f, 0x24ff6e20, 0x740c40b9, 0x026a5503, 0x194b2a57, 0x7a9a1cff)

  /** F_R1 and F_R2 values after Initialization mode */
  val R1_post_init = 0x860a7dfa
  val R2_post_init = 0xbf0e0ffc

  /** W value after Initialization mode */
  val W_post_init = 0xa2ec3df2
  /**Key Stream Length*/
  val KSlen : Int = 32
  /**Generated Key stream*/
  val Z_post_gen: Seq[Int] = Seq(0x14f1c272, 0x3279c419, 0x4b8ea41d, 0xcc80863, 0xd28062e1, 0xe71d3dda, 0xe3c4d158, 0xa7f067ac, 0x94935056, 0x8ee5c63d, 0xf5a0cec3, 0xd33da5a7, 0x7de892ac, 0xe8fd9b12, 0xfb625a84, 0xf15a5323, 0xd93d3995, 0x9a485a71, 0xdab8ecd1, 0x9d9b3e2e, 0x169e4914, 0xb82f20a, 0x38362744, 0xe56d8cde, 0xe65def83, 0x5ac36fe7, 0x1065d63e, 0x9cf995ca, 0x2c0511d9, 0xe25f710a, 0xfa5e2af7, 0xcf39e148)
}

object zuc128_ScalaModelTestData4 {
  val key: Seq[Int] = Seq(0x4d ,0x32, 0x0b, 0xfa, 0xd4, 0xc2, 0x85, 0xbf, 0xd6, 0xb8, 0xbd, 0x00 ,0xf3 ,0x9d ,0x8b, 0x41)
  val IV : Seq[Int]= Seq(0x52 ,0x95, 0x9d, 0xab, 0xa0, 0xbf, 0x17, 0x6e, 0xce, 0x2d, 0xc3, 0x15, 0x04, 0x9e, 0xb5, 0x74)

  /**LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x26c4d752, 0x1926bc95, 0x05e26b9d, 0x7d135eab, 0x6a5789a0, 0x6135e2bf ,
  0x42f13517, 0x5f89af6e,0x6b4d78ce, 0x5c2f132d, 0x5eebc4c3, 0x001af115, 0x79de2604, 0x4ebc4d9e, 0x45f89ab5,
    0x20c7ac74)

  /**LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x1f808882, 0x4fc08639, 0x246a9891, 0x1f77c16f, 0x50f0e1c9, 0x723e8fac,
    0x24334616, 0x4471b734,0x7dba1992, 0x25180096, 0x4637117c, 0x2a92aac8, 0x7da8d7b5,
    0x58f45afe, 0x42814800, 0x56d7e7d8)

  /** F_R1 and F_R2 values after Initialization mode */
  val R1_post_init = 0x52761a25
  val R2_post_init = 0x38f712e1

  /** W value after Initialization mode */
  val W_post_init = 0x20eebfab
  /**Key Stream Length*/
  val KSlen : Int = 32
  /**Generated Key stream*/
  val Z_post_gen: Seq[Int] = Seq(0xed4400e7, 0x633e5c5, 0xb28ea9ba, 0x22414181, 0xcbab6263, 0x955e04ae, 0x84b5fb47, 0xe90ebf63, 0xdbe3ad7, 0x575fd35a, 0xd498f5a3, 0x2befcae0, 0x90068fe4, 0x6048c7ab, 0xe4ad6e5d, 0x4e78863f, 0x2dd9ff19, 0x492bb532, 0x444d4ba8, 0x94d7470e, 0x4e519459, 0x9e0913c0, 0xd109438c, 0xeb5cb91a, 0x6982522d, 0xf127c8f, 0xd38505f, 0xb84b0013, 0x4b55fea1, 0x2d46f55e, 0xaa75843, 0x426ce70d)
}

class ZUC_128_ModelTester extends FreeSpec with ChiselScalatestTester {

  "Initial LFSR value for the initialization mode. TC:1" in {
    assert(zuc128_model.init_LFSR_key_exp(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV) == zuc128_ScalaModelTestData1.LFSR_init)
  }
  "LFSR value for the POST initialization mode. TC:1" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV)
    println(s"Getting LFSR_S to be: ${zuc128_model.LFSR_S}\n Whereas expected value is: ${zuc128_ScalaModelTestData1.LFSR_post_init}")
    assert(zuc128_model.LFSR_S == zuc128_ScalaModelTestData1.LFSR_post_init)
  }
  "F_R(1) value after the initialization mode. TC:1" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV)
    assert(zuc128_model.F_R(0) == zuc128_ScalaModelTestData1.R1_post_init)
  }
  "F_R(2) value after the initialization mode. TC:1" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV)
    assert(zuc128_model.F_R(1) == zuc128_ScalaModelTestData1.R2_post_init)
  }
  "W value after the initialization mode. TC:1" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV)
    assert(zuc128_model.w == zuc128_ScalaModelTestData1.W_post_init)
  }
  "Keystream generated. TC:1" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData1.key, zuc128_ScalaModelTestData1.IV)
    assert(zuc128_model.GenerateKeystream(zuc128_ScalaModelTestData1.KSlen) == zuc128_ScalaModelTestData1.Z_post_gen)
  }


  "Initial LFSR value for the initialization mode. TC:2" in {
    assert(zuc128_model.init_LFSR_key_exp(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV) == zuc128_ScalaModelTestData2.LFSR_init)
  }
  "LFSR value for the POST initialization mode. TC:2" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV)
    println(s"Getting LFSR_S to be: ${zuc128_model.LFSR_S}\n Whereas expected value is: ${zuc128_ScalaModelTestData2.LFSR_post_init}")
    assert(zuc128_model.LFSR_S == zuc128_ScalaModelTestData2.LFSR_post_init)
  }
  "F_R(1) value after the initialization mode. TC:2" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV)
    assert(zuc128_model.F_R(0) == zuc128_ScalaModelTestData2.R1_post_init)
  }
  "F_R(2) value after the initialization mode. TC:2" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV)
    assert(zuc128_model.F_R(1) == zuc128_ScalaModelTestData2.R2_post_init)
  }
  "W value after the initialization mode. TC:2" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV)
    assert(zuc128_model.w == zuc128_ScalaModelTestData2.W_post_init)
  }
  "Keystream generated. TC:2" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData2.key, zuc128_ScalaModelTestData2.IV)
    assert(zuc128_model.GenerateKeystream(zuc128_ScalaModelTestData2.KSlen) == zuc128_ScalaModelTestData2.Z_post_gen)
  }

  "Initial LFSR value for the initialization mode. TC:3" in {
    assert(zuc128_model.init_LFSR_key_exp(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV) == zuc128_ScalaModelTestData3.LFSR_init)
  }
  "LFSR value for the POST initialization mode. TC:3" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV)
    println(s"Getting LFSR_S to be: ${zuc128_model.LFSR_S}\n Whereas expected value is: ${zuc128_ScalaModelTestData3.LFSR_post_init}")
    assert(zuc128_model.LFSR_S == zuc128_ScalaModelTestData3.LFSR_post_init)
  }
  "F_R(1) value after the initialization mode. TC:3" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV)
    assert(zuc128_model.F_R(0) == zuc128_ScalaModelTestData3.R1_post_init)
  }
  "F_R(2) value after the initialization mode. TC:3" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV)
    assert(zuc128_model.F_R(1) == zuc128_ScalaModelTestData3.R2_post_init)
  }
  "W value after the initialization mode. TC:3" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV)
    assert(zuc128_model.w == zuc128_ScalaModelTestData3.W_post_init)
  }
  "Keystream generated. TC:3" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData3.key, zuc128_ScalaModelTestData3.IV)
    assert(zuc128_model.GenerateKeystream(zuc128_ScalaModelTestData3.KSlen) == zuc128_ScalaModelTestData3.Z_post_gen)
  }


  "Initial LFSR value for the initialization mode. TC:4" in {
    assert(zuc128_model.init_LFSR_key_exp(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV) == zuc128_ScalaModelTestData4.LFSR_init)
  }
  "LFSR value for the POST initialization mode. TC:4" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV)
    println(s"Getting LFSR_S to be: ${zuc128_model.LFSR_S}\n Whereas expected value is: ${zuc128_ScalaModelTestData4.LFSR_post_init}")
    assert(zuc128_model.LFSR_S == zuc128_ScalaModelTestData4.LFSR_post_init)
  }
  "F_R(1) value after the initialization mode. TC:4" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV)
    assert(zuc128_model.F_R(0) == zuc128_ScalaModelTestData4.R1_post_init)
  }
  "F_R(2) value after the initialization mode. TC:4" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV)
    assert(zuc128_model.F_R(1) == zuc128_ScalaModelTestData4.R2_post_init)
  }
  "W value after the initialization mode. TC:4" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV)
    assert(zuc128_model.w == zuc128_ScalaModelTestData4.W_post_init)
  }
  "Keystream generated. TC:4" in {
    zuc128_model.Initialization(zuc128_ScalaModelTestData4.key, zuc128_ScalaModelTestData4.IV)
    assert(zuc128_model.GenerateKeystream(zuc128_ScalaModelTestData4.KSlen) == zuc128_ScalaModelTestData4.Z_post_gen)
  }
}

