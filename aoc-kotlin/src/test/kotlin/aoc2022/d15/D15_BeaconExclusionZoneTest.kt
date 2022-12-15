package aoc2022.d15

import aoc.Coordinate
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
		assertEquals(13, testSensors[6].findCoveredPositions(10).count())
	}

	@Test
	fun findBeaconExclusionTestCount() {
		assertEquals(26, findBeaconExclusions(testSensors, 10).bitSet.cardinality())
	}

	@Test
	fun findBeaconExclusionCount() {
		assertEquals(4502208, findBeaconExclusions(sensors, 2000000).bitSet.cardinality())
	}

	@Test
	fun findTestBeaconLocation() {
		assertEquals(Coordinate(14, 11), findBeaconLocation(testSensors, 20, 20))
	}

	@Test
	fun findTestFreq() {
		findBeaconLocation(testSensors, 20, 20)?.let {
			assertEquals(56000011, tuningFrequency(it))
		}
	}

	@Test
	fun findFreq() {
		findBeaconLocation(sensors, 4000000, 4000000)?.let {
			assertEquals(0, tuningFrequency(it))
		}
	}
}
