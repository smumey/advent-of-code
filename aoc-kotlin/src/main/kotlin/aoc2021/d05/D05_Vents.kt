package aoc2021.d05

import kotlin.math.abs

private data class Point(val x: Int, val y: Int)

private data class Line(val begin: Point, val end: Point) {

	private fun isVertical(): Boolean {
		return begin.x == end.x;
	}

	private fun isHorizontal(): Boolean {
		return begin.y == end.y
	}

	private fun isDiagonal(): Boolean {
		return abs(begin.x - end.x) == abs(begin.y - end.y)
	}

	private fun diagPoints(): List<Point> {
		return (0..abs(begin.x - end.x))
			.map {
				Point(begin.x + signum(end.x - begin.x) * it, begin.y + signum(end.y - begin.y) * it)
			}
	}

	fun points(): List<Point> {
		val points = if (isHorizontal()) toRange(begin.x, end.x).map { Point(it, begin.y) }
		else
			if (isVertical()) toRange(begin.y, end.y).map { Point(begin.x, it) }
			else
				if (isDiagonal()) diagPoints()
				else listOf()
		return points
	}
}

private fun signum(i: Int): Int = if (i < 0) -1 else if (i > 0) 1 else 0

private fun toRange(x: Int, y: Int): IntRange {
	return if (x < y) x..y else y..x
}

private fun parseLine(line: String): Line {
	val (p1, p2) = line.split(" -> ")
		.map { p ->
			val (x, y) = p.split(",").map(String::toInt)
			Point(x, y)
		}
	return Line(p1, p2)
}

fun main() {
	val lines = generateSequence(::readLine).map(::parseLine).toList()
	println(
		lines
			.flatMap(Line::points)
			.groupBy({ it }, { it })
			.mapValues { (_, points) -> points.size }
			.filter { (_, count) -> count > 1 }
			.size
	)
}
