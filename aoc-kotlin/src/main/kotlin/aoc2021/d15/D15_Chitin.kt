package aoc2021.d15

import java.util.*
import kotlin.collections.ArrayDeque

data class Point(val x: Int, val y: Int)

fun cost(grid: List<List<Int>>, path: List<Point>): Int {
	return path.subList(1, path.size).sumOf { grid[it.y][it.x] }
}

fun isInGrid(grid: List<List<Int>>, point: Point): Boolean {
	return point.x in grid.first().indices &&
			point.y in grid.indices
}

fun neighbours(grid: List<List<Int>>, point: Point): Set<Point> {
	return sequenceOf(
		Point(point.x - 1, point.y),
		Point(point.x + 1, point.y),
		Point(point.x, point.y - 1),
		Point(point.x, point.y + 1)
	)
		.filter { isInGrid(grid, it) }
		.toSet()
}

fun findRoute(grid: List<List<Int>>): Pair<List<Point>, Int> {
	val distances = grid.map { r -> r.map { Int.MAX_VALUE }.toIntArray() }
	distances[0][0] = 0
	val previous = grid.map { r -> r.map { null as Point? }.toMutableList() }
	val queue = ArrayDeque(grid.indices.flatMap { y -> grid[0].indices.map { x -> Point(x, y)}.toList() })
	while (queue.isNotEmpty()) {
		val p = queue.removeFirst()
		neighbours(grid, p)
			.filter(queue::contains)
			.forEach {
				val dist = distances[p.y][p.x] + grid[it.y][it.x]
				if (dist < distances[it.y][it.x]) {
					distances[it.y][it.x] = dist
					previous[it.y][it.x] = p
				}
			}
	}
	return Pair(
		(generateSequence(previous.last().last() ?: Point(0, 0)) { (previous[it.y][it.x]) }).toList().reversed(),
		distances.last().last()
	)
}

fun main() {
	val grid = generateSequence(::readLine).map { l ->
		l.map { it.digitToInt() }
	}.toList()
	print(findRoute(grid))
}
