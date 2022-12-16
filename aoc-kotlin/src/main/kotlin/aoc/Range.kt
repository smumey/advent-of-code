package aoc

fun distinctRanges(ranges: List<IntRange>): List<IntRange> {
    val sorted = ranges.sortedBy { it.first }
    return sorted.fold(listOf<IntRange>()) { distinct, range ->
        if (distinct.isEmpty()) listOf(range)
        else {
            val lastRange: IntRange = distinct.last()
            if (lastRange.last >= range.first - 1) {
                if (lastRange.last >= range.last) {
                    distinct
                } else {
                    distinct.dropLast(1).plusElement(lastRange.first..range.last)
                }
            } else {
                distinct.plusElement(range)
            }
        }
    }
}
