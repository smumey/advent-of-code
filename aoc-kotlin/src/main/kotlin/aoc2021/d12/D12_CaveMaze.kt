package aoc2021.d12

import java.time.Duration
import kotlin.system.measureTimeMillis

const val START = "start"
const val FINISH = "end"

private fun isSmall(loc: String): Boolean {
	return loc.all(Char::isLowerCase)
}

fun allowSmallAdd(loc: String, currentSmalls: Pair<Boolean, Set<String>>): Boolean {
	return !(currentSmalls.first && currentSmalls.second.contains(loc))
}

private fun allowAdd(path: Pair<List<String>, Pair<Boolean, Set<String>>>, loc: String): Boolean {
	return if (loc == START) false
	else if (isSmall(loc)) {
		allowSmallAdd(loc, path.second)
	} else true
}

private fun addSmall(currentSmalls: Pair<Boolean, Set<String>>, loc: String): Pair<Boolean, Set<String>> {
	return if (currentSmalls.first) {
		Pair(true, currentSmalls.second + loc)
	} else if (currentSmalls.second.contains(loc)) {
		Pair(true, currentSmalls.second)
	} else {
		Pair(false, currentSmalls.second + loc)
	}
}

private fun search(locMap: Map<String, Set<String>>): List<List<String>> {
	val routes = mutableListOf<List<String>>()
	val paths = ArrayDeque(listOf(Pair(listOf(START), Pair(false, setOf<String>()))))
	while (paths.isNotEmpty()) {
		val path = paths.removeFirst()
		for (loc in locMap.getOrDefault(path.first.last(), listOf())) {
			if (loc == FINISH) {
				routes.add(path.first + loc)
			} else if (allowAdd(path, loc)) {
				paths.add(Pair(path.first + loc, if (isSmall(loc)) addSmall(path.second, loc) else path.second))
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
	val result: List<List<String>>
	val time = measureTimeMillis { result = search(locMap) }
	println(result)
	println(result.size)
	println(Duration.ofMillis(time))
}
