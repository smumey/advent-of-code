package aoc2022.d12

import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D12_HillClimbingTest {
	val topoMap = parse(readTestInput("aoc2022/12"))

	@Test
	fun findShortestPath() {
		val start = topoMap.find('S')
		val end = topoMap.find('E')
		if (start != null && end != null) {
			assertEquals(31, topoMap.findShortestPath(start, end).size)
		}
	}

	@Test
	fun findShortestLowPath() {
		val end = topoMap.find('E')
		if (end != null) {
			assertEquals(29, topoMap.findShortestLowPath(end))
		}
	}
}
