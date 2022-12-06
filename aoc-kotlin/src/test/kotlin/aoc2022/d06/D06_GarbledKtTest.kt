package aoc2022.d06

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class D06_GarbledKtTest {
	@Test
	fun findStartOfPacketMarker1() {
		assertEquals(7, findStartOfPacketMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
	}

	@Test
	fun findStartOfPacketMarker2() {
		assertEquals(5, findStartOfPacketMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
	}

	@Test
	fun findStartOfMessageMarker1() {
		assertEquals(19, findStartOfMessageMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
	}

	@Test
	fun findStartOfMessageMarker2() {
		assertEquals(23, findStartOfMessageMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
	}
}