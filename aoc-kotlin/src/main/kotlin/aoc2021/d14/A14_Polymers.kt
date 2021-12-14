package aoc2021.d14

fun step(template: String, rules: Map<String, String>): String {
	return template.foldIndexed("") { i, polymer, element ->
		if (i < template.length - 1) {
			val pair = template.substring(i, i + 2)
			polymer + element + rules.getOrDefault(pair, "")
		} else {
			polymer + element
		}
	}
}

fun main() {
	val templatePolymer = readLine() ?: ""
	readLine()
	val insertionRules = generateSequence(::readLine)
		.map { l ->
			val (pair, insert) = l.split(" -> ")
			Pair(pair, insert)
		}
		.toMap()
	println(insertionRules)
	val polymer = (1..10).fold(templatePolymer) { p, _ -> step(p, insertionRules) }
	val counts = polymer.groupBy { it }.mapValues { it.value.size }.values
	println(counts.maxOrNull()?.minus(counts.minOrNull() ?: 0))
}
