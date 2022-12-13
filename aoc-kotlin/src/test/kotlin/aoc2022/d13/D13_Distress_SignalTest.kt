package aoc2022.d13

import aoc.readTestInput
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class D13_Distress_SignalTest {
	val packetPairs = toPacketPairs(readTestInput("aoc2022/13").mapNotNull(::parse))

	@Test
	fun compare0() {
		assertTrue(packetPairs[0][0] < packetPairs[0][1])
	}

	@Test
	fun compare1() {
		assertTrue(packetPairs[1][0] < packetPairs[1][1])
	}

	@Test
	fun compare2() {
		assertTrue(packetPairs[2][0] > packetPairs[2][1])
	}

	@Test
	fun compare7() {
		assertTrue(packetPairs[7][0] > packetPairs[7][1])
	}

	@Test
	fun sumCorrectOrderIndices() {
		assertEquals(13, sumCorrectOrderIndices(packetPairs))
	}
}
