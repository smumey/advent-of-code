package aoc2021

import kotlin.math.abs

fun price(position: Int, target: Int): Int {
	val d = abs(position - target)
	return (d * (d + 1)) / 2
}

fun main() {
	val positions = readLine()?.split(",")?.map(String::toInt) ?: listOf()
	val min = positions.minOrNull() ?: 0
	val max = positions.maxOrNull() ?: 0
	println(
		(min..max).fold(Pair(-1, Int.MAX_VALUE)) { current, target ->
			val newCost = positions.sumOf { price(it, target) }
			if (newCost < current.second) Pair(target, newCost) else current
		}
	)
}
