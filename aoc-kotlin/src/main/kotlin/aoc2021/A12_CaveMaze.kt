package aoc2021

import kotlin.collections.ArrayDeque

private fun isSmall(loc: String): Boolean {
	return loc.all(Char::isLowerCase)
}

private fun allowAdd(newPath: Pair<List<String>, Map<String, Int>>, start: String): Boolean {
	val loc = newPath.first.last()
	return if (loc == start) false
	else if (isSmall(loc)) {
		newPath.second.count { e -> e.value > 1 } < 2 && newPath.second.all { e -> e.value <= 2 }
	} else true
}

private fun addCount(map: Map<String, Int>, loc: String): Map<String, Int> {
	val newMap = map.toMutableMap()
	newMap.merge(loc, 1, Int::plus)
	return newMap
}

private fun search(start: String, finish: String, locMap: Map<String, Set<String>>): List<List<String>> {
	val routes = mutableListOf<List<String>>()
	val paths = ArrayDeque(listOf(Pair(listOf(start), mapOf<String, Int>())))
	while (paths.isNotEmpty()) {
		val path = paths.removeFirst()
		for (loc in locMap.getOrDefault(path.first.last(), listOf())) {
			val newPath = Pair(path.first + loc, if(isSmall(loc)) addCount(path.second, loc) else path.second)
			if (loc == finish) {
				routes.add(newPath.first)
			} else if (allowAdd(newPath, start)) {
				paths.add(newPath)
			}
		}
	}
	return routes
}

fun main() {
	val locMap = generateSequence(::readLine)
		.map {
			val (from, to) = it.split("-")
			Pair(from, to)
		}
		.fold(mutableMapOf<String, MutableSet<String>>()) { m, p ->
			m.compute(p.first) { _, v ->
				if (v == null) {
					mutableSetOf(p.second)
				} else {
					v.add(p.second)
					v
				}
			}
			m.compute(p.second) { _, v ->
				if (v == null) {
					mutableSetOf(p.first)
				} else {
					v.add(p.first)
					v
				}
			}
			m
		}
	println(search("start", "end", locMap).size)
}
