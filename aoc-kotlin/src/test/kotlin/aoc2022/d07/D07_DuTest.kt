package aoc2022.d07

import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D07_DuTest {
	val lines = readTestInput("aoc2022/7")

	@Test
	fun parse() {
		val root = parse(lines)
		println(root)
	}

	@Test
	fun sumSmall() {
		assertEquals(95437L, parse(lines).sumSmall(100_000L).first)
	}

	@Test
	fun findSmallest() {
		assertEquals(24933642L, findMinDelete(parse(lines), 30_000_000L))
	}
}
