package aoc2021.d08

data class Reading(val inputs: Set<String>, val outputs: List<String>)

fun toReading(line: String): Reading {
	val (inputS, outputS) = line.split(" | ")
	return Reading(
		inputS.split(" ").map { it.asSequence().sorted().joinToString("") }.toSet(),
		outputS.split(" ").map { it.asSequence().sorted().joinToString("") }
	)
}

fun deduce(inputs: Set<String>): Map<String, Int> {
	val remaining = inputs.toMutableList()
	val one = remaining.first { it.length == 2 }
	remaining.remove(one)
	val four = remaining.first { it.length == 4 }
	remaining.remove(four)
	val seven = remaining.first { it.length == 3 }
	remaining.remove(seven)
	val eight = inputs.first { it.length == 7 }
	remaining.remove(eight)
	val six = remaining.first { it.length == 6 && it.toSet().subtract(one.toSet()).size == 5 }
	remaining.remove(six)
	val nine = remaining.first { it.length == 6 && it.toSet().subtract(four.toSet()).size == 2 }
	remaining.remove(nine)
	val zero = remaining.first { it.length == 6 && it.toSet().subtract(four.toSet()).size == 3 }
	remaining.remove(zero)
	val three = remaining.first { it.length == 5 && it.toSet().subtract(one.toSet()).size == 3 }
	remaining.remove(three)
	val five = remaining.first { it.length == 5 && six.toSet().subtract(it.toSet()).size == 1 }
	remaining.remove(five)
	val two = remaining.first()
	remaining.remove(two)
	return mapOf(
		zero to 0,
		one to 1,
		two to 2,
		three to 3,
		four to 4,
		five to 5,
		six to 6,
		seven to 7,
		eight to 8,
		nine to 9
	)
}

fun main() {
	val readings = generateSequence(::readLine).map(::toReading).toList()
	println(
		readings
			.map {
				val decipher = deduce(it.inputs)
				it.outputs.map { code -> decipher[code] }
					.fold(0) { value, digit -> 10 * value + (digit ?: Int.MAX_VALUE) }
			}
			.sum()
	)
}
