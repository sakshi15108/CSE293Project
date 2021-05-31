package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer

class ZUC128Tester extends FreeSpec with ChiselScalatestTester {

  def testZUC128(Key : Seq[UInt], Iv : Seq[UInt], keystreamlen : Int , outkeystream : Seq[SInt],Key_num : Int,parallelism :Int): Boolean = {
    assert(Key.length == Key_num)
    assert(Iv.length == Key_num)
    val p = zucParams(keystreamlen,Key_num,parallelism)
    test(new zuc128(p)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      /** Idle state of Hardware */
      dut.clock.step()
      /** Providing valid inputs by indicating valid of input as true */
      dut.io.in.valid.poke(true.B)
      dut.clock.step()
      /** Loading Key and IV inputs to Hardware  */
      dut.io.in.bits.key.zip(Key).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
      dut.io.in.bits.IV.zip(Iv).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
      dut.clock.step(parallelism)

      /**After 1st clock expecting key/IV to load and R0 and R1 set to 0 */
      dut.clock.step(32)
      /** Expecting output to be not valid after Initialization mode  */
      dut.io.KeyStream.valid.expect(false.B)
      /**working stage complete after this clock*/
      dut.clock.step()
      /**compare pkeystream after keystreamlen cycles*/
      for(i <- 0 until keystreamlen){
        dut.clock.step()
        dut.io.KeyStream.bits.expect(outkeystream(i))
        /** Expecting output to be valid when generating each 32 bit words of KeyStream */
        dut.io.KeyStream.valid.expect(true.B)
      }

    }
    true
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 1 (no parallelism)" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData1.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV , zuc128_ScalaModelTestData1.KSlen, outkeystream,16,1)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 2 (no parallelism)" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData2.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key,IV,zuc128_ScalaModelTestData2.KSlen,outkeystream,16,1)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 3 (no parallelism)" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData3.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData3.KSlen,outkeystream,16,1)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 4 (no parallelism)" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData4.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData4.KSlen,outkeystream,16,1)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 1 (parallelism=2, HW requirement reduced, number of cycles increased by 2)" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData1.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV , zuc128_ScalaModelTestData1.KSlen, outkeystream,16,2)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 2 (parallelism=2, HW requirement reduced, number of cycles increased by 2)" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData2.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key,IV,zuc128_ScalaModelTestData2.KSlen,outkeystream,16,2)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 3 (parallelism=2, HW requirement reduced, number of cycles increased by 2)" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData3.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData3.KSlen,outkeystream,16,2)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 4 (parallelism=2, HW requirement reduced, number of cycles increased by 2)" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData4.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData4.KSlen,outkeystream,16,2)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 1 (full parallelism; minimum HW requirement)" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData1.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV , zuc128_ScalaModelTestData1.KSlen, outkeystream,16,16)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 2 (full parallelism; minimum HW requirement)" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData2.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key,IV,zuc128_ScalaModelTestData2.KSlen,outkeystream,16,16)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 3 (full parallelism; minimum HW requirement)" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData3.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData3.KSlen,outkeystream,16,16)
  }
  "Hardware ZUC128 should generate correct keystream for Test Vector 4 (full parallelism; minimum HW requirement)" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData4.Z_post_gen.map(_.S)
    /** For 128 bit(16 bytes) keys key_num is 16; so if you want to make it ZUC 256 , key_num will be 32*/
    testZUC128(key, IV,zuc128_ScalaModelTestData4.KSlen,outkeystream,16,16)
  }
}