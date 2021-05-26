package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer

class ZUC128Tester extends FreeSpec with ChiselScalatestTester {

  def testZUC128(Key : Seq[UInt], Iv : Seq[UInt], keystreamlen : Int , outkeystream : Seq[SInt]): Boolean = {
    assert(Key.length == 16)
    assert(Iv.length == 16)
    val p = zucParams(keystreamlen)
    test(new zuc128(p)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.step() // idle state of hardware
      dut.io.in.valid.poke(true.B)
      dut.clock.step()
      dut.io.in.bits.key.zip(Key).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
      dut.io.in.bits.IV.zip(Iv).foreach{ case (dutIO, elem) => dutIO.poke(elem) }
      dut.clock.step()

      println(" After 1st clock expecting key/IV to load and R0 and R1 set to 0 ")
      // check LFSR values after 1 + 32 cycles
      for(i <- 0 until 32){dut.clock.step()}
      dut.io.KeyStream.valid.expect(false.B)
      println(" After 32 clocks of init mode ")
      //working stage complete after this clock
      dut.clock.step()
      println(" After working mode of 1 clock cycle")
      //compare pkeystream after keystreamlen cycles
      for(i <- 0 until keystreamlen){
        dut.clock.step()
        dut.io.KeyStream.bits.expect(outkeystream(i))
        dut.io.KeyStream.valid.expect(true.B)
      }

    }
    true
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 1" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData1.Z_post_gen.map(_.S)
    testZUC128(key, IV , zuc128_ScalaModelTestData1.KSlen, outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 2" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData2.Z_post_gen.map(_.S)
    testZUC128(key,IV,zuc128_ScalaModelTestData2.KSlen,outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 3" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData3.Z_post_gen.map(_.S)
    testZUC128(key, IV,zuc128_ScalaModelTestData3.KSlen,outkeystream)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 4" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    val outkeystream = zuc128_ScalaModelTestData4.Z_post_gen.map(_.S)
    testZUC128(key, IV,zuc128_ScalaModelTestData4.KSlen,outkeystream)
  }

}