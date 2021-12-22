package aoc2021.d21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class D21_GodDiceKtTest {
	@Test
	fun testCalculateProduct() {
		assertEquals(739785, calculateProduct(listOf(3, 8)))
	}
}
