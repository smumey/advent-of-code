package aoc2021.d14

private fun step(pairCounts: Map<String, Long>, rules: Map<String, List<String>>): Map<String, Long> {
	return pairCounts.entries.fold(mapOf()) { c, entry ->
		val pair = entry.key
		val counts = c.toMutableMap()
		val pairs = rules.getOrDefault(pair, listOf(pair))
		pairs.forEach { p -> counts.merge(p, entry.value) { old, new -> new + old } }
		counts
	}
}

private fun toPairCounts(polymer: String): Map<String, Long> {
	return polymer.foldIndexed(mapOf()) { i, map, _ ->
		val m = map.toMutableMap()
		if (i < polymer.length - 1) {
			val pair = polymer.substring(i, i + 2)
			m.merge(pair, 1) { c1, c2 -> c1 + c2 }
			m
		} else m
	}
}

private fun toElementCounts(polymer: String, pairCounts: Map<String, Long>): Map<Char, Long> {
	val elCounts = mutableMapOf<Char, Long>()
	pairCounts.forEach() { entry -> elCounts.merge(entry.key[0], entry.value) { c1, c2 -> c1 + c2 } }
	elCounts.merge(polymer[polymer.length - 1], 1) { c1, c2 -> c1 + c2 }
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
