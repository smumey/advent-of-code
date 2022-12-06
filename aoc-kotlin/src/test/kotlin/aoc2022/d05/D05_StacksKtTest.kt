package aoc2022.d05

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import readTestInput

class D05_StacksKtTest {
	private val input = parse(readTestInput("aoc2022/5"))

	@Test
	fun message() {
		println(input.second)
		assertEquals("CMZ", applyMoves(input.first, input.second).message())
	}

	@Test
	fun messageBatch() {
		println(input.second)
		assertEquals("MCD", applyBatchMoves(input.first, input.second).message())
	}
}