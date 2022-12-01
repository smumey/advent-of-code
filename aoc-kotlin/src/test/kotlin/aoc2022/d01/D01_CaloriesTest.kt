package aoc2022.d01

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

private val INPUT = """
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
    """.trimIndent().lines()

internal class D01_CaloriesTest {
	@Test
	fun findMaximum() {
        assertEquals(24000, findMaximum(parse(INPUT.asSequence())))
	}

    fun findSumOfTop3() {
        assertEquals(45000, findSumOfTop3(parse(INPUT.asSequence())))
    }
}
