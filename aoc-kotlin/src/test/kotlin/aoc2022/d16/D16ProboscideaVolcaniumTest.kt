package aoc2022.d16

import aoc.readInput
import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D16ProboscideaVolcaniumTest {
	val testValves = readTestInput("aoc2022/16-test").map(::parse)
	val valves = readInput("aoc2022/16").map(::parse)

	@Test
	fun parse() {
		println(testValves)

		println("functional valves count ${valves.count { it.rate > 0 }}")
	}

	@Test
	fun findDistance() {
		testValves.find { it.label == "JJ" }?.let {
			println(findDistance(testValves, getConnect(testValves), it))
		}
	}

	@Test
	fun findMaxPressureTest() {
		assertEquals(1651, findMaxPressure(testValves, "AA"))
	}

	@Test
	fun findMaxPressure() {
		assertEquals(2087, findMaxPressure(valves, "AA"))
	}

	@Test
	fun findMaxPressureWithElephantsTest() {
		assertEquals(1707, findMaxPressureWithElephant(testValves, "AA", 26))
	}

	@Test
	fun findMaxPressureWithElephants() {
		assertEquals(2591, findMaxPressureWithElephant(valves, "AA", 26))
	}
}
