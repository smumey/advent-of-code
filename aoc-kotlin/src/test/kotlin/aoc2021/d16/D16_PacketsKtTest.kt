package aoc2021.d16

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class D16_PacketsKtTest {
	@Test
	fun testRead() {
		val result = toBinaryString(toIntArray("D2FE28"))
		assertEquals("110100101111111000101000", result)
	}

	@Test
	fun testLiteral() {
		val hex = "D2FE28"
		val packet = readPacketFromHex(hex)
		assertEquals(2021, packet.evaluate())
	}

	@Test
	fun testSumVersions_1() {
		val packet = readPacketFromHex("8A004A801A8002F478")
		assertEquals(16, sumVersions(packet))
	}

	@Test
	fun testSumVersions_2() {
		val packet = readPacketFromHex("620080001611562C8802118E34")
		assertEquals(12, sumVersions(packet))
	}

	@Test
	fun testEvaluate_1() {
		val packet = readPacketFromHex("C200B40A82")
		assertEquals(3L, packet.evaluate())
	}

	@Test
	fun testEvaluate_2() {
		val packet = readPacketFromHex("04005AC33890")
		assertEquals(54L, packet.evaluate())
	}

	@Test
	fun testEvaluate_3() {
		val packet = readPacketFromHex("CE00C43D881120")
		assertEquals(9L, packet.evaluate())
	}

	@Test
	fun testEvaluate_4() {
		val packet = readPacketFromHex("D8005AC2A8F0")
		assertEquals(1L, packet.evaluate())
	}

	@Test
	fun testEvaluate_5() {
		val packet = readPacketFromHex("F600BC2D8F")
		assertEquals(0L, packet.evaluate())
	}

	@Test
	fun testEvaluate_6() {
		val packet = readPacketFromHex("9C005AC2F8F0")
		assertEquals(0L, packet.evaluate())
	}

	@Test
	fun testEvaluate_7() {
		val packet = readPacketFromHex("9C0141080250320F1802104A08")
		assertEquals(1L, packet.evaluate())
	}
}
