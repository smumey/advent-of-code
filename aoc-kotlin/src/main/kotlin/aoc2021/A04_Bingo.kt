package aoc2021

data class Card(val numbers: List<Int>) {
    fun wins(called: List<Int>): Boolean {
        return sequence {
            yieldAll(rows())
            yieldAll(columns())
        }
            .filter { line -> called.containsAll(line) }
            .any()
    }

    private fun rows(): Sequence<List<Int>> {
        return (0..4).asSequence()
            .map { i -> numbers.subList(5 * i, 5 * (i + 1)) }
    }

    private fun columns(): Sequence<List<Int>> {
        return (0..4).asSequence()
            .map { i -> numbers.filterIndexed { j, n -> j % 5 == i } }
    }

    fun score(called: List<Int>): Int {
        return numbers.subtract(called.toSet()).sum() * called.last()
    }
}

fun readCard(): Card? {
    println("reading card")
    val header = readLine()
    return if (header == null) null
    else Card(
        (0..4)
            .flatMap {
                val l = readLine()
                println(l)
                l.orEmpty().trim().split(" +".toRegex()).map(String::toInt)
            }
            .toList()
    )
}

fun main() {
    val called = readLine().orEmpty().split(",").map(String::toInt)
    val cards: List<Card> = generateSequence(::readCard).toList()
    println(
        (5 until called.size)
            .map { called.subList(0, it) }
            .flatMap { cards.filter { card -> card.wins(it) }.map { card -> card.score(it) } }
            .firstOrNull()
    )
}
