package aoc2022.d11

import aoc.readTestInput
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class D11_MonkeyingAroundTest {
	val state = parse(readTestInput("aoc2022/11"))

	@Test
	fun parse() {
		println(state)
	}

	@Test
	fun processRound() {
		println(executeRound(state.roundState, state.monkeys))
	}

	@Test
	fun findActiveMonkeyScore() {
		assertEquals(
			10605,
			findActiveMonkeyScore(executeRounds(state.roundState, state.monkeys, 20).inspectCounts)
		)
	}
}
