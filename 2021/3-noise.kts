data class Trace(val digits: Int, val value: Int)

fun parseTrace(line: String): Trace {
    return Trace(line.length, line.toInt(2))
}

fun gamma(traces: List<Trace>): Int {
    val digits = traces[0].digits
    val length = traces.size
    var g = 0
    for (d in 0 until digits) {
        val sum = traces.asSequence()
            .map({ trace -> (trace.value ushr d) and 1 })
            .sum()
        g += if (2 * sum == length) {
            throw Exception("bad count ${sum} for ${length} length")
        } else if (2 * sum > length) {
            1 shl d
        } else {
            0
        }
    }
    return g
}

fun epsilon(traces: List<Trace>): Int {
    val mask = (1 shl traces[0].digits)  - 1
    return (gamma(traces) xor -1) and mask
}


fun main() {
    val traces = generateSequence(::readLine)
        .map(::parseTrace)
        .toList()
    println(gamma(traces) * epsilon(traces))
}

main()
