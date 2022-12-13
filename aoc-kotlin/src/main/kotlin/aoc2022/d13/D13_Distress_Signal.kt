package aoc2022.d13

import aoc.readInput

data class Packet(val list: List<Any>) : Comparable<Packet> {
	override fun compareTo(other: Packet): Int {
		return listCompare(this.list, other.list)
	}
}

fun listCompare(left: List<*>, right: List<*>): Int {
	if (left.isEmpty()) {
		return if (right.isEmpty()) 0
		else -1
	} else if (right.isEmpty()) {
		return 1
	}
	val leftEl = left[0]
	val rightEl = right[0]
	val elCompare = when {
		leftEl is Int && rightEl is Int -> leftEl.compareTo(rightEl)
		leftEl is List<*> && rightEl is List<*> -> listCompare(leftEl, rightEl)
		leftEl is Int && rightEl is List<*> -> listCompare(listOf(leftEl), rightEl)
		leftEl is List<*> && rightEl is Int -> listCompare(leftEl, listOf(rightEl))
		else -> throw RuntimeException()
	}
	return if (elCompare == 0) listCompare(left.drop(1), right.drop(1)) else elCompare

}

fun parse(line: String): Packet? {
	if (line.isEmpty()) return null
	val stack = mutableListOf<MutableList<Any>>(mutableListOf())
	var value: Int? = null
	for (char in line) {
		when (char) {
			'[' -> {
				val list = mutableListOf<Any>()
				stack.last().add(list)
				stack.add(list)
			}

			',' -> {
				value?.let { stack.last().add(it) }
				value = null
			}

			']' -> {
				value?.let { stack.last().add(it) }
				stack.removeLast()
				value = null
			}

			else -> {
				value = (if (value == null) 0 else value * 10) + char.digitToInt()
			}
		}
	}
	return Packet(stack[0][0] as List<Any>)
}

fun toPacketPairs(packets: List<Packet>): List<List<Packet>> {
	return packets.windowed(2, 2)
}

fun sumCorrectOrderIndices(packetPairs: List<List<Packet>>): Int {
	return packetPairs.mapIndexed { i, pair -> if (pair[0] < pair[1]) i + 1 else 0 }.sum()
}

fun main() {
	println(sumCorrectOrderIndices(toPacketPairs(readInput("aoc2022/13").mapNotNull(::parse))))
}
