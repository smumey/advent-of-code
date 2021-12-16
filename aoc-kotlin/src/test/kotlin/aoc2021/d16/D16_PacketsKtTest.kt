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
	fun testSumVersions_1() {
		val packet = readPacketFromHex("8A004A801A8002F478")
		assertEquals(16, sumVersions(packet))
	}

	@Test
	fun testSumVersions_2() {
		val packet = readPacketFromHex("620080001611562C8802118E34")
		println(packet)
		assertEquals(12, sumVersions(packet))
	}
}
