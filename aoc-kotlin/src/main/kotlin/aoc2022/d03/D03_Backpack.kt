package aoc2022.d03

import readInput

data class Backpack(val first: String, val second: String) {
    fun duplicate() = first.find(second::contains)

    fun itemSet() = (first + second).toSet()
}

fun priority(item: Char) = when (item) {
    in 'a'..'z' -> item - 'a' + 1
    in 'A'..'Z' -> item - 'A' + 27
    else -> Int.MIN_VALUE
}

fun parse(line: String): Backpack {
    val first = line.substring(0, line.length / 2)
    val second = line.substring(line.length / 2)
    return Backpack(first, second)
}

fun sumPriorities(backpacks: List<Backpack>): Int = backpacks.mapNotNull(Backpack::duplicate).sumOf { priority(it) }

fun splitIntoGroups(backpacks: List<Backpack>): List<List<Backpack>> {
    return backpacks.windowed(3, 3, true)
}

fun findBadge(backpacks: List<Backpack>) =
    backpacks.map(Backpack::itemSet).reduce { s1, s2 -> s1.intersect(s2) }.first()

fun main() {
    println(
        splitIntoGroups(readInput("aoc2022/3").map { parse(it) })
            .mapNotNull { findBadge(it) }
            .sumOf { priority(it) }
    )
}