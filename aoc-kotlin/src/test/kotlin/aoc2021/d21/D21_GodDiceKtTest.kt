package aoc2021.d21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class D21_GodDiceKtTest {
	@Test
	fun testCalculateProduct() {
		val (_, endState) = calculateGameStates(listOf(4, 8)).last()
		assertEquals(444356092776315L, findMostWins(endState))
	}
}
