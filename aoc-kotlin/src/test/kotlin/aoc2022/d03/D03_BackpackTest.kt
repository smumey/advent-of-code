package aoc2022.d03

import org.junit.jupiter.api.Test
import readTestInput
import kotlin.test.assertEquals

internal class D03_BackpackTest {
    private val backpacks = readTestInput("aoc2022/3").map { parse(it) }

    @Test
    fun sumPriorities() {
        assertEquals(157, sumPriorities(backpacks))
    }

    @Test
    fun findBadgeSum() {
        assertEquals(70, splitIntoGroups(backpacks).mapNotNull { findBadge(it) }.sumOf { priority(it) })
    }
}