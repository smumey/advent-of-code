package aoc2022.d16

import aoc.readInput
import aoc.readTestInput
import org.junit.jupiter.api.Test

class D16_ProboscideaVolcaniumTest {
	val testValves = readTestInput("aoc2022/16-test").map(::parse)
	val valves = readInput("aoc2022/16").map(::parse)

	@Test
	fun parse() {
		println(testValves)

		println("functional valves count ${valves.count { it.rate > 0 }}")
	}
}
