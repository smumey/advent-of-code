package aoc2022.d01

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class D01_CaloriesTest {
	@Test
	fun findMaximum() {
		val input = """
    1000
    2000
    3000

    4000

    5000
    6000

    7000
    8000
    9000

    10000
    """.trimIndent()
        assertEquals(24000, findMaximum(parse(input.lines().asSequence())))
	}
}
