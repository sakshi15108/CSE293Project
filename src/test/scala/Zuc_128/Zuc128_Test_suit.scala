package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer


class ZUC_128_ModelTester extends FreeSpec with ChiselScalatestTester {
  implicit def int2Byte(i: Int) : Byte = i.toByte//from: https://stackoverflow.com/questions/48926339/scala-hex-literal-for-bytes


  val key : Seq[Byte] = Seq(0x3d, 0x4c, 0x4b, 0xe9, 0x6a, 0x82, 0xfd, 0xae, 0xb5, 0x8f, 0x64, 0x1d, 0xb1, 0x7b ,0x45,0x5b)
  val IV : Seq[Byte]= Seq(0x84 ,0x31 ,0x9a ,0xa8 ,0xde ,0x69 ,0x15 ,0xca ,0x1f ,0x6b ,0xda ,0x6b ,0xfb ,0xd8 ,0xc7 ,0x66)
  /*LFSR-state at the beginning:*/
  val LFSR_init :Seq[Int] = Seq(0x1ec4d784, 0x2626bc31, 0x25e26b9a, 0x74935ea8, 0x355789de, 0x4135e269, 0x7ef13515, 0x5709afca,
    0x5acd781f, 0x47af136b, 0x326bc4da, 0x0e9af16b, 0x58de26fb, 0x3dbc4dd8, 0x22f89ac7, 0x2dc7ac66)

  /*LFSR-state after completion of the initialisation mode:*/
  val LFSR_post_init :Seq[Int] = Seq(0x10da5941, 0x5b6acbf6, 0x17060ce1, 0x35368174, 0xf4385a, 0x479943df, 0x2753bab2, 0x73775d6a,
    0x43930a37, 0x77b4af31, 0x15b2e89f, 0x24ff6e20, 0x740c40b9, 0x026a5503, 0x194b2a57, 0x7a9a1cff)

  val R1_post_init = 0x860a7dfa
  val R2_post_init = 0xbf0e0ffc

  val W_post_init = 0xa2ec3df2

  println("1.\n")
  assert(zuc128_model.init_LFSR_key_exp(key, IV) == LFSR_init)
  zuc128_model.Initialization(key, IV)
  println(s"Getting LFSR_S to be: ${zuc128_model.LFSR_S}\n Whereas expected value is: ${LFSR_post_init}")
  println("2.\n")
  assert(zuc128_model.LFSR_S == LFSR_post_init)
  println("3.\n")
  assert(zuc128_model.F_R(0) == R1_post_init)
  println("4.\n")
  assert(zuc128_model.F_R(1) == R2_post_init)
  println("5.\n")
  assert(zuc128_model.F() == W_post_init)

  val Z_post_gen = 0x14f1c272
  println("6.\n")
  assert(zuc128_model.GenerateKeystream(1) == Z_post_gen)

}
