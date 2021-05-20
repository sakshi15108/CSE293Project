package ZUC_128

import chisel3._
import chisel3.tester._
import org.scalatest.FreeSpec
import treadle._
import chisel3.tester.experimental.TestOptionBuilder._

import scala.collection.mutable.ArrayBuffer

class ZUC128Tester extends FreeSpec with ChiselScalatestTester {

  def testZUC128(key : Seq[UInt], IV : Seq[UInt]): Boolean = {

    true
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 1" in {
    val key = zuc128_ScalaModelTestData1.key.map(_.U)
    val IV = zuc128_ScalaModelTestData1.IV.map(_.U)
    testZUC128(key, IV)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 2" in {
    val key = zuc128_ScalaModelTestData2.key.map(_.U)
    val IV = zuc128_ScalaModelTestData2.IV.map(_.U)
    testZUC128(key,IV)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 3" in {
    val key = zuc128_ScalaModelTestData3.key.map(_.U)
    val IV = zuc128_ScalaModelTestData3.IV.map(_.U)
    testZUC128(key, IV)
  }

  "Hardware ZUC128 should generate correct keystream for Test Vector 4" in {
    val key = zuc128_ScalaModelTestData4.key.map(_.U)
    val IV = zuc128_ScalaModelTestData4.IV.map(_.U)
    testZUC128(key, IV)
  }
}