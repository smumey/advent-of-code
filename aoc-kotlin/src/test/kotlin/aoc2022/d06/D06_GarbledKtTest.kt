package aoc2022.d06

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class D06_GarbledKtTest {
	@Test
	fun findMarker1() {
		assertEquals(7, findMarker("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
	}

	@Test
	fun findMarker2() {
		assertEquals(5, findMarker("bvwbjplbgvbhsrlpgdmjqwftvncz"))
	}
}