package aoc2022.d06

import aoc.readInput

fun findStartOfPacketMarker(message: String): Int? {
	return (4..message.length).find { message.substring(it - 4, it).toSet().size == 4 }
}

fun findStartOfMessageMarker(message: String): Int? {
	return (14..message.length).find { message.substring(it - 14, it).toSet().size == 14 }
}

fun main() {
	println(findStartOfMessageMarker(readInput("aoc2022/6")[0]))
}
