package aoc2022.d02

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class D02_RpsTest {
    val INPUT = """
        A Y
        B X
        C Z
    """.trimIndent().lines()

    @Test
    fun evaluateRounds() {
        assertEquals(15, evaluateRounds(INPUT.asSequence().map { l -> parse(l) }))
    }

    @Test
    fun evaluatePlans() {
        assertEquals(12, evaluatePlans(INPUT.asSequence().map { l -> parsePlan(l) }))
    }
}