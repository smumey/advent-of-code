package aoc2021

private fun isSmall(loc: String): Boolean {
	return loc.all(Char::isLowerCase)
}

private fun allowAdd(newPath: List<String>, start: String): Boolean {
	val loc = newPath.last()
	return if (loc == start) false
	else if (isSmall(loc)) {
		val smalls = newPath.subList(1, newPath.size).filter(::isSmall)
		smalls.toSet().size >= smalls.size - 1
	} else true
}

private fun search(start: String, finish: String, locMap: Map<String, Set<String>>): List<List<String>> {
	val routes = mutableListOf<List<String>>()
	val paths = mutableListOf(listOf(start))
	while (paths.isNotEmpty()) {
		val path = paths.removeFirst()
//		println(path)
		for (loc in locMap.getOrDefault(path.last(), listOf())) {
			val newPath = path + loc
			if (loc == finish) {
//				println("adding route $newPath")
				routes.add(newPath)
			} else if (allowAdd(newPath, start)) {
//				println("adding path $newPath")
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
	println(locMap)
	println(search("start", "end", locMap).size)
}
