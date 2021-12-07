package aoc2021

import kotlin.math.abs


fun main() {
	val positions = readLine()?.split(",")?.map(String::toInt) ?: listOf()
	val mid = positions.sorted()[positions.size/2] // sum / positions.size + if ((sum % positions.size) * 2 > positions.size) 1 else 0
	println(positions.fold(0) { distance, pos -> distance + abs(pos - mid) })
}
