package aoc2021.d03

data class Trace(val bits: Int, val value: Int)

fun parseTrace(line: String): Trace {
	return Trace(line.length, line.toInt(2))
}

fun pick(value: Int, bit: Int): Int {
	return (value ushr bit) and 1
}

fun eval(traces: List<Trace>, compare: (Int, Int) -> Boolean, bit: Int): Int {
	val length = traces.size
	if (length == 1) {
		return traces[0].value
	}

	val sum = traces.asSequence()
		.map { trace -> pick(trace.value, bit) }
		.sum()

	val predicate = { trace: Trace ->
		val v = trace.value
		pick(v, bit) == if (compare(2 * sum, length)) 1 else 0
	}

	return eval(traces.asSequence().filter(predicate).toList(), compare, bit - 1)
}

fun oxygen(traces: List<Trace>): Int {
	return eval(traces, { x, y -> x >= y }, traces[0].bits - 1)
}

fun co2(traces: List<Trace>): Int {
	return eval(traces, { x, y -> x < y }, traces[0].bits - 1)
}

fun main() {
	val traces = generateSequence(::readLine)
		.map(::parseTrace)
		.toList()
	println(oxygen(traces) * co2(traces))
}
