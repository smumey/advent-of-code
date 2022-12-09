package aoc2022.d09

import aoc.readTestInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class D09_SnakeRopeTest {
	val input = readTestInput("aoc2022/9").map(::parse)

	@Test
	fun countTailPositions() {
		assertEquals(13, countTailPositions(input))
	}
}
