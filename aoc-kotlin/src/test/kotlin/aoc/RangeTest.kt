package aoc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RangeTest {
    @Test
    fun distinctRanges() {
        assertEquals(listOf(1..5), distinctRanges(listOf(4..5, 2..4, 1..1)))
    }

    @Test
    fun distinctRangesSep() {
        assertEquals(listOf(1..5, 7..10), distinctRanges(listOf(4..5, 1..5, 7..10)))
    }

    @Test
    fun distinctRangesFullyOverlapped() {
        assertEquals(listOf(1..5), distinctRanges(listOf(1..5, 3..4)))
    }
}
