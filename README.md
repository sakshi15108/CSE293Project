Chisel Project For CSE293
=========================

Following are the deliverables:
1.	Scala model: https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc_128.scala
2.	Scala test: https://github.com/sakshi15108/CSE293Project/blob/main/src/test/scala/Zuc_128/Zuc128_Test_suit.scala
3.	Chisel Implementation: https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc128HW.scala
4.	Chisel test: https://github.com/sakshi15108/CSE293Project/blob/main/src/test/scala/Zuc_128/zuc128HW_Test.scala

**Project details:**

Algorithm implemented: 
  ``ZUC-128``

``ZUC`` is a word-oriented stream cipher. It takes a 128-bit initial key and a 128-bit initial vector (IV) as input, and outputs a keystream of 32-bit words (where each 32-bit word is hence called a key-word). This keystream can be used for encryption/decryption.

The execution of ZUC has two stages: initialization stage and working stage. In the first stage, a key/IV initialization is performed, i.e., the cipher is clocked without producing output. The second stage is a working stage. In this stage, with every clock pulse, it produces a 32-bit word of output.

We have completed the Scala implementation along with necessary helper functions and testing with stages broken down into testing the input to all stages(LFSR, F(), before and after initialization mode). Finally the required output (keystream) generated is tested.

For our Chisel implementation, we have used FSM and plan to make the IO decoupled in order to enable its smooth interfacing with any bigger application like 3GPP Confidentiality and Integrity Algorithms. Since the scope of this project limits implementation of applications using the Stream Cipher, we intend to make the ZUC-128 Stream Cipher well enough to be used as a submodule without requiring changes for interfacing with any application module.

**Testing the code:**

4 set of test vectors have been used to test the [Scala implementation](https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc_128.scala) exhaustively. These can all be run using : ``testOnly ZUC_128.ZUC_128_ModelTester``
If you want to execute individual tests, you can run ``testOnly ZUC_128.ZUC_128_ModelTester -- -t "<name of the test>"``. For example: ``testOnly ZUC_128.ZUC_128_ModelTester -- -t "LFSR value for the POST initialization mode. TC:1"``will run [a test for test case 1](https://github.com/sakshi15108/CSE293Project/blob/27ac44f4a0284a8d5d15e276e60407ee0dc4ef92/src/test/scala/Zuc_128/Zuc128_Test_suit.scala#L98).

The basic working algorithm has been [implemented in Chisel](https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc128HW.scala) as well. Testing of this implementation can be executed by running: ``testOnly ZUC_128.ZUC128Tester``.
If you want to execute individual tests, you can run ``testOnly ZUC_128.ZUC128Tester -- -t "<name of the test>"``. For example: ``testOnly ZUC_128.ZUC_128_ModelTester -- -t "Hardware ZUC128 should generate correct keystream for Test Vector 1"``will run [a test for test case 1](https://github.com/sakshi15108/CSE293Project/blob/27ac44f4a0284a8d5d15e276e60407ee0dc4ef92/src/test/scala/Zuc_128/zuc128HW_Test.scala#L43).


References:
[1] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 1: 128-EEA3 and 128-EIA3 Specifications.
[2] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 2: ZUC Specification.
[3] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 3: Implementor’s Test Data
