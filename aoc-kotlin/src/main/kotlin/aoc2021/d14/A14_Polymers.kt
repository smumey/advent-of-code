package aoc2021.d14

private fun step(pairCounts: Map<String, Long>, rules: Map<String, List<String>>): Map<String, Long> {
	return pairCounts.entries.fold(mapOf()) { c, entry ->
		val pair = entry.key
		val counts = c.toMutableMap()
		val pairs = rules.getOrDefault(pair, listOf(pair))
		pairs.forEach { p -> counts.merge(p, entry.value, Long::plus) }
		counts
	}
}

private fun toPairCounts(polymer: String): Map<String, Long> {
	return polymer.foldIndexed(mapOf()) { i, map, _ ->
		if (i < polymer.length - 1) {
			val pair = polymer.substring(i, i + 2)
			val m = map.toMutableMap()
			m.merge(pair, 1, Long::plus)
			m
		} else map
	}
}

private fun toElementCounts(polymer: String, pairCounts: Map<String, Long>): Map<Char, Long> {
	val elCounts = mutableMapOf<Char, Long>()
	pairCounts.forEach() { entry -> elCounts.merge(entry.key[0], entry.value, Long::plus) }
	elCounts.merge(polymer[polymer.length - 1], 1, Long::plus)
	return elCounts
}

fun main() {
	val templatePolymer = readLine() ?: ""
	readLine()
	val insertionRules = generateSequence(::readLine)
		.map { l ->
			val (pair, insert) = l.split(" -> ")
			Pair(pair, listOf(pair[0] + insert, insert + pair[1]))
		}
		.toMap()
	val polymerCounts = (1..40).fold(toPairCounts(templatePolymer)) { p, _ -> step(p, insertionRules) }
	val elementCounts = toElementCounts(templatePolymer, polymerCounts)
	val counts = elementCounts.values
	println(counts.maxOrNull()?.minus(counts.minOrNull() ?: 0))
}
