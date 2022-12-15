package aoc2022.d15

import aoc.readInput
import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D15_BeaconExclusionZoneTest {
	val testSensors = readTestInput("aoc2022/15").map(::parse)
	val sensors = readInput("aoc2022/15").map(::parse)

	@Test
	fun test() {
		println(testSensors)
	}

	@Test
	fun findCoveredPositions() {
		assertEquals(13, testSensors[6].findCoveredPositions(10).size)
	}

	@Test
	fun findBeaconExclusionTestCount() {
		assertEquals(26, findBeaconExclusionCount(testSensors, 10))
	}

	@Test
	fun findBeaconExclusionCount() {
		assertEquals(0, findBeaconExclusionCount(sensors, 2000000))
	}
}
