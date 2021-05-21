Chisel Project For CSE293
=========================

The repo has been set as public. Following are the deliverables:
1.	Scala model: https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc_128.scala
2.	Scala test: https://github.com/sakshi15108/CSE293Project/blob/main/src/test/scala/Zuc_128/Zuc128_Test_suit.scala
3.	Chisel Implementation: https://github.com/sakshi15108/CSE293Project/blob/main/src/main/scala/ZUC_128/zuc128HW.scala
4.	Chisel test: https://github.com/sakshi15108/CSE293Project/blob/main/src/test/scala/Zuc_128/zuc128HW_Test.scala

**Project details:**

Algorithm implemented: 
  ``ZUC-128``

``ZUC`` is a word-oriented stream cipher. It takes a 128-bit initial key and a 128-bit initial vector (IV) as input, and outputs a keystream of 32-bit words (where each 32-bit word is hence called a key-word). This keystream can be used for encryption/decryption.

The execution of ZUC has two stages: initialization stage and working stage. In the first stage, a key/IV initialization is performed, i.e., the cipher is clocked without producing output. The second stage is a working stage. In this stage, with every clock pulse, it produces a 32-bit word of output.

References:
[1] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 1: 128-EEA3 and 128-EIA3 Specifications.
[2] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 2: ZUC Specification.
[3] Specification of the 3GPP Confidentiality and Integrity Algorithms 128-EEA3 & 128-EIA3. Document 3: Implementor’s Test Data
