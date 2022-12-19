package aoc2022.d17

import aoc.readInput
import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D17PyroclasticFlowTest {
	val testWinds = parse(readTestInput("aoc2022/17-test")[0])
	val winds = parse(readInput("aoc2022/17")[0])

	@Test
	fun testRun() {
		val cave = Cave(testWinds)
		cave.processRock()
		cave.processRock()
		println(cave.floors)
	}

	@Test
	fun testFloor() {
		val cave = Cave(testWinds)
		cave.processRock()
		assertEquals(1, cave.floors.size)
		assertEquals(listOf(false, false, true, true, true, true, false), cave.floors.last())
		cave.processRock()
		assertEquals(4, cave.floors.size)
		assertEquals(listOf(false, false, false, true, false, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(6, cave.floors.size)
		assertEquals(listOf(false, false, true, false, false, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(7, cave.floors.size)
		assertEquals(listOf(false, false, false, false, true, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(9, cave.floors.size)
		assertEquals(listOf(false, false, false, false, true, true, false), cave.floors.last())
		cave.processRock()
		assertEquals(10, cave.floors.size)
		assertEquals(listOf(false, true, true, true, true, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(13, cave.floors.size)
		assertEquals(listOf(false, false, true, false, false, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(15, cave.floors.size)
		assertEquals(listOf(false, false, false, false, false, true, false), cave.floors.last())
		cave.processRock()
		assertEquals(17, cave.floors.size)
		assertEquals(listOf(false, false, false, false, true, false, false), cave.floors.last())
		cave.processRock()
		assertEquals(17, cave.floors.size)
		assertEquals(listOf(false, false, false, false, true, false, false), cave.floors.last())
	}

	@Test
	fun testGetHeight() {
		assertEquals(1, getHeight(1, testWinds))
		assertEquals(4, getHeight(2, testWinds))
		assertEquals(6, getHeight(3, testWinds))
		assertEquals(7, getHeight(4, testWinds))
		assertEquals(9, getHeight(5, testWinds))
		assertEquals(3068, getHeight(2022, testWinds))
	}

	@Test
	fun getHeight() {
		assertEquals(3114, getHeight(2022, winds))
	}

	@Test
	fun testGetHeightLarge() {
		assertEquals(1514285714288L, getHeightLarge(1000000000000L, testWinds))
	}

	@Test
	fun getHeightLarge() {
		assertEquals(1540804597682L, getHeightLarge(1000000000000L, winds))
	}
}
