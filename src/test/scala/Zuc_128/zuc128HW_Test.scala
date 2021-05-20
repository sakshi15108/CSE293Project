package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer

class ZUC128Tester extends FreeSpec with ChiselScalatestTester {

  def testZUC128(Key : Seq[UInt], IV : Seq[UInt], keystreamlen : Int , outkeystream : Seq[UInt]): Boolean = {
    val p = zucParams(keystreamlen)
    test(new zuc128(p)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      Key.zip(IV) foreach { case (aChunk, bChunk) =>
        dut.io.key.zip(aChunk).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
        dut.io.IV.zip(bChunk).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
      }
      //after fisrt clock cycle expecting key/IV to load and R0 and R1 set to 0
      dut.clock.step()
      // check LFSR values after 1 + 32 cycles
      for(i <- 0 until 33){dut.clock.step()}
      //working stage complete after this clock
      dut.clock.step()
      //compare pkeystream after keystreamlen cycles
      for(i <- 0 until keystreamlen){dut.clock.step()}
      dut.io.KeyStream.expect(outkeystream)
    }
    true
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 1" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData1.Z_post_gen.map(_.U)
    testZUC128(key, IV , 1, outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 2" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData2.Z_post_gen.map(_.U)
    testZUC128(key,IV,1,outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 3" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData3.Z_post_gen.map(_.U)
    testZUC128(key, IV,1,outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 4" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData4.Z_post_gen.map(_.U)
    testZUC128(key, IV,1,outkeystream)
  }
}