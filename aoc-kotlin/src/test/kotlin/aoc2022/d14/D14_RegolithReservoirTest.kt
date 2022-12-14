package aoc2022.d14

import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D14_RegolithReservoirTest {
	val rockCoordinates = readTestInput("aoc2022/14").map(::parse).flatMap(::filled).toSet()

	@Test
	fun sandRestVoidCount() {
		val initialState = SandState(maxDepth(rockCoordinates), rockCoordinates, sandSource, 0)
		assertEquals(24, generateSequence(initialState) { it.stepVoid() }.last().sandRestCount)
	}

	@Test
	fun sandRestInfiniteFloorCount() {
		val initialState = SandState(maxDepth(rockCoordinates), rockCoordinates, sandSource, 0)
		assertEquals(93, generateSequence(initialState) { it.stepInfiniteFloor() }.last().sandRestCount)
	}
}
