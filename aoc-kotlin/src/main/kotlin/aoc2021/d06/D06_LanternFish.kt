package aoc2021.d06

private fun advance(counts: Map<Int, Long>): Map<Int, Long> {
	return counts.entries.fold(mutableMapOf()) { nc, entry ->
		val (stage, count) = entry
		if (stage > 0) {
			nc[stage - 1] = (nc[stage - 1] ?: 0L) + count
		} else {
			nc[6] = (nc[6] ?: 0L) + count
			nc[8] = count
		}
		nc
	}
}

fun main() {
	val start = readLine().orEmpty().split(",").map(String::toInt)
		.groupBy({ it }, { it })
		.mapValues { (_, fishes) -> fishes.size.toLong() }
	println(
		generateSequence(start, ::advance).elementAt(256).values.sum()
	)
}
