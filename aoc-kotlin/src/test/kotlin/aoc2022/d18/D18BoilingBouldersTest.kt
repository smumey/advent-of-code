package aoc2022.d18

import aoc.readInput
import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D18BoilingBouldersTest {
	val testCubes = readTestInput("aoc2022/18-test").map(::parse)
	val cubes = readInput("aoc2022/18").map(::parse)

	@Test
	fun testSurfaceArea() {
		assertEquals(64, surfaceArea(testCubes))
	}

	@Test
	fun surfaceArea() {
		assertEquals(3856, surfaceArea(cubes))
	}
}
