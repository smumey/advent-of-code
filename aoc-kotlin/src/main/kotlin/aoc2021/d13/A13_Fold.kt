package aoc2021.d13

private data class Point(val x: Int, val y: Int)

private fun reflectY(points: Set<Point>, line: Int): Set<Point> {
	return points.map { if (it.y > line) Point(it.x, line - (it.y - line)) else it }
		.toSet()
}

private fun reflectX(points: Set<Point>, line: Int): Set<Point> {
	return points.map { if (it.x > line) Point(line - (it.x - line), it.y) else it }
		.toSet()
}

val foldRe = "^fold along ([xy])=(\\d+)".toRegex()

fun main() {
	val lines = generateSequence(::readLine).toList()
	val points = lines
		.takeWhile(String::isNotEmpty)
		.map { l ->
			var (x, y) = l.split(",").map(String::toInt)
			Point(x, y)
		}.toSet()
	println(points)
	val folds = lines.map {
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
	}.filterNotNull()
	println(folds.first()(points).count())
}
