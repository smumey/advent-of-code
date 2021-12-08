package aoc2021

data class Reading(val inputs: List<String>, val outputs: List<String>)

fun toReading(line: String): Reading {
	val (inputS, outputS) = line.split(" | ")
	return Reading(inputS.split(" "), outputS.split(" "))
}

fun main() {
	val readings = generateSequence(::readLine).map(::toReading).toList()
	println(readings)
	println(
		readings
			.flatMap { r -> r.outputs }
			.count { w -> setOf(2, 4, 3, 7).contains(w.length) }
	)
}
