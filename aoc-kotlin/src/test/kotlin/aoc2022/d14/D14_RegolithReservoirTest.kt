package aoc2022.d14

import aoc.readInput
import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D14_RegolithReservoirTest {
	val rockTestCoordinates = readTestInput("aoc2022/14").map(::parse).flatMap(::filled).toSet()
	val rockCoordinates = readInput("aoc2022/14").map(::parse).flatMap(::filled).toSet()

	@Test
	fun sandRestVoidCount() {
		val initialState = SandState(maxDepth(rockTestCoordinates), rockTestCoordinates, sandSource, 0)
		assertEquals(24, generateSequence(initialState) { it.stepVoid() }.last().sandRestCount)
	}

	@Test
	fun sandRestInfiniteFloorCountTest() {
		val initialState = SandState(maxDepth(rockTestCoordinates), rockTestCoordinates, sandSource, 0)
		assertEquals(93, generateSequence(initialState) { it.stepInfiniteFloor() }.last().sandRestCount)
	}

	@Test
	fun sandRestInfiniteFloorCount() {
		val initialState = SandState(maxDepth(rockCoordinates), rockCoordinates, sandSource, 0)
		assertEquals(22499, generateSequence(initialState) { it.stepInfiniteFloor() }.last().sandRestCount)
	}
}
