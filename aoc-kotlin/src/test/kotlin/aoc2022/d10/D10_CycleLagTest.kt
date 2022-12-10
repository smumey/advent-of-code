package aoc2022.d10

import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D10_CycleLagTest {
	val input = readTestInput("aoc2022/10").map(::parse)

	@Test
	fun calculateRegisterValues() {
		println(calculateRegisterValues(input))
	}

	@Test
	fun calculateSignalStringSum() {
		assertEquals(13140, calculateSignalStrengthSum(calculateRegisterValues(input)))
	}

	@Test
	fun render() {
		print(render(calculateRegisterValues(input)))
	}
}
