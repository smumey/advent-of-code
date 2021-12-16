package aoc2021.d13

private data class Point(val x: Int, val y: Int)

private fun reflectY(points: Set<Point>, line: Int): Set<Point> {
	return points.map { if (it.y > line) Point(it.x, line - (it.y - line)) else it }.toSet()
}

private fun reflectX(points: Set<Point>, line: Int): Set<Point> {
	return points.map { if (it.x > line) Point(line - (it.x - line), it.y) else it }.toSet()
}

private fun toGrid(points: Set<Point>): List<List<Char>> {
	val xRange = points.fold(IntRange(Int.MAX_VALUE, Int.MIN_VALUE)) { range, p ->
		val newMin = if (p.x < range.first) p.x else range.first
		val newMax = if (range.last < p.x) p.x else range.last
		newMin..newMax
	}
	val yRange = points.fold(IntRange(Int.MAX_VALUE, Int.MIN_VALUE)) { range, p ->
		val newMin = if (p.y < range.first) p.y else range.first
		val newMax = if (range.last < p.y) p.y else range.last
		newMin..newMax
	}
	return yRange.map { y ->
		xRange.map { x ->
			if (points.contains(Point(x, y))) '#' else ' '
		}
	}
}

val foldRe = "^fold along ([xy])=(\\d+)".toRegex()

fun main() {
	val lines = generateSequence(::readLine).toList()
	val points = lines
		.takeWhile(String::isNotEmpty)
		.map { l ->
			val (x, y) = l.split(",").map(String::toInt)
			Point(x, y)
		}.toSet()
	val folds = lines.mapNotNull {
		val match = foldRe.find(it)
		if (match == null) null
		else {
			val (_, coord, value) = match.groupValues
			val coordVal = value.toInt()
			when (coord) {
				"x" -> { points: Set<Point> -> reflectX(points, coordVal) }
				"y" -> { points: Set<Point> -> reflectY(points, coordVal) }
				else -> null
			}
		}
	}

	toGrid(folds.fold(points) { p, f -> f(p) }).forEach { line ->
		println(line.joinToString(""))
	}
}
