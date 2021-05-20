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

  val LFSR_init :Seq[BigInt] = Seq(0x0044d700, 0x0026bc00, 0x00626b00, 0x00135e00, 0x00578900,
    0x0035e200, 0x00713500, 0x0009af00, 0x004d7800, 0x002f1300, 0x006bc400, 0x001af100, 0x005e2600,
    0x003c4d00, 0x00789a00, 0x0047ac00)

  val LFSR_post_init :Seq[BigInt] = Seq(0x7ce15b8b, 0x747ca0c4, 0x6259dd0b, 0x47a94c2b, 0x3a89c82e,
    0x32b433fc, 0x231ea13f, 0x31711e42,0x4ccce955, 0x3fb6071e, 0x161d3512, 0x7114b136, 0x5154d452,
    0x78c69a74, 0x4f26ba6b, 0x3e1b8d6a)

  val R1_post_init = 0x14cfd44c
  val R2_post_init = 0x8c6de800

  val W_post_init = 0x1b85d1e6
  val KSlen : Int = 1
  val Z_post_gen: Seq[BigInt] = Seq(0x27bede74)

}

object zuc128_ScalaModelTestData2{
  val key : Seq[Int] = Seq(0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff ,0xff,0xff)
  val IV : Seq[Int]= Seq(0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff ,0xff)

  /*LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x7fc4d7ff, 0x7fa6bcff, 0x7fe26bff, 0x7f935eff, 0x7fd789ff, 0x7fb5e2ff,
    0x7ff135ff, 0x7f89afff, 0x7fcd78ff, 0x7faf13ff, 0x7febc4ff, 0x7f9af1ff, 0x7fde26ff, 0x7fbc4dff,
    0x7ff89aff, 0x7fc7acff)

  /*LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x09a339ad, 0x1291d190, 0x25554227, 0x36c09187, 0x0697773b,
    0x443cf9cd, 0x6a4cd899, 0x49e34bd0,0x56130b14, 0x20e8f24c, 0x7a5b1dcc, 0x0c3cc2d1, 0x1cc082c8,
    0x7f5904a2, 0x55b61ce8, 0x1fe46106)

  val R1_post_init = 0xb8017bd5
  val R2_post_init = 0x9ce2de5c

  val W_post_init = 0xfce125a7
  val KSlen : Int = 1
  val Z_post_gen: Seq[BigInt] = Seq(0x0657cfa0)
}

object zuc128_ScalaModelTestData3 {

  val key : Seq[Int] = Seq(0x3d, 0x4c, 0x4b, 0xe9, 0x6a, 0x82, 0xfd, 0xae, 0xb5, 0x8f, 0x64, 0x1d, 0xb1, 0x7b ,0x45,0x5b)
  val IV : Seq[Int]= Seq(0x84 ,0x31 ,0x9a ,0xa8 ,0xde ,0x69 ,0x15 ,0xca ,0x1f ,0x6b ,0xda ,0x6b ,0xfb ,0xd8 ,0xc7 ,0x66)
  /*LFSR-state at the beginning:*/
  val LFSR_init :Seq[BigInt] = Seq(0x1ec4d784, 0x2626bc31, 0x25e26b9a, 0x74935ea8, 0x355789de, 0x4135e269, 0x7ef13515, 0x5709afca,
    0x5acd781f, 0x47af136b, 0x326bc4da, 0x0e9af16b, 0x58de26fb, 0x3dbc4dd8, 0x22f89ac7, 0x2dc7ac66)
  /*LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[BigInt] = Seq(0x10da5941, 0x5b6acbf6, 0x17060ce1, 0x35368174, 0x5cf4385a, 0x479943df, 0x2753bab2, 0x73775d6a,
    0x43930a37, 0x77b4af31, 0x15b2e89f, 0x24ff6e20, 0x740c40b9, 0x026a5503, 0x194b2a57, 0x7a9a1cff)

  val R1_post_init = 0x860a7dfa
  val R2_post_init = 0xbf0e0ffc

  val W_post_init = 0xa2ec3df2
  val KSlen : Int = 1
  val Z_post_gen: Seq[BigInt] = Seq(0x14f1c272)
}

object zuc128_ScalaModelTestData4 {
  val key: Seq[Int] = Seq(0x4d ,0x32, 0x0b, 0xfa, 0xd4, 0xc2, 0x85, 0xbf, 0xd6, 0xb8, 0xbd, 0x00 ,0xf3 ,0x9d ,0x8b, 0x41)
  val IV : Seq[Int]= Seq(0x52 ,0x95, 0x9d, 0xab, 0xa0, 0xbf, 0x17, 0x6e, 0xce, 0x2d, 0xc3, 0x15, 0x04, 0x9e, 0xb5, 0x74)

  val LFSR_init :Seq[BigInt] = Seq(0x26c4d752, 0x1926bc95, 0x05e26b9d, 0x7d135eab, 0x6a5789a0, 0x6135e2bf ,
  0x42f13517, 0x5f89af6e,0x6b4d78ce, 0x5c2f132d, 0x5eebc4c3, 0x001af115, 0x79de2604, 0x4ebc4d9e, 0x45f89ab5,
    0x20c7ac74)

  val LFSR_post_init :Seq[BigInt] = Seq(0x1f808882, 0x4fc08639, 0x246a9891, 0x1f77c16f, 0x50f0e1c9, 0x723e8fac,
    0x24334616, 0x4471b734,0x7dba1992, 0x25180096, 0x4637117c, 0x2a92aac8, 0x7da8d7b5,
    0x58f45afe, 0x42814800, 0x56d7e7d8)

  val R1_post_init = 0x52761a25
  val R2_post_init = 0x38f712e1

  val W_post_init = 0x20eebfab
  val KSlen : Int = 1
  val Z_post_gen: Seq[BigInt] = Seq(0xed4400e7)
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

