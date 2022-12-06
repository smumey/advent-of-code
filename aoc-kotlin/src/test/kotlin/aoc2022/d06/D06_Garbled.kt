package aoc2022.d06

import readInput

fun findMarker(message: String): Int? {
	return (4..message.length).find { message.substring(it - 4, it).toSet().size == 4 }
}

fun main() {
	println(findMarker(readInput("aoc2022/6")[0]))
}