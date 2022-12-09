package aoc2022.d08

import aoc.readTestInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class D08_TreeFortTest {
	val trees = parse(readTestInput("aoc2022/8"))

	@Test
	fun interveningTrees_left() {
		assertEquals(listOf(Coordinate(0, 0)), trees.interveningTrees(Coordinate(1, 0), Direction.LEFT).toList())
	}

	@Test
	fun interveningTrees_up() {
		assertEquals(listOf(Coordinate(0, 0)), trees.interveningTrees(Coordinate(0, 1), Direction.UP).toList())
	}

	@Test
	fun countVisible() {
		assertEquals(21, trees.countVisible())
	}

	@Test
	fun scenicScore() {
		assertEquals(4, trees.scenicScore(Coordinate(2, 1)))
	}

	@Test
	fun maxScenicScore() {
		assertEquals(8, trees.maxScenicScore())
	}
}
