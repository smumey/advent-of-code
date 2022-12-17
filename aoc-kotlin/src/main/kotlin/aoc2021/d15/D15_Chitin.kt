package aoc2021.d15

import java.time.Duration
import kotlin.system.measureTimeMillis

data class Point(val x: Int, val y: Int)

fun isInGrid(point: Point, maxPoint: Point): Boolean {
	return point.x in 0..maxPoint.x &&
			point.y in 0..maxPoint.y
}

fun neighbours(point: Point, maxPoint: Point): Set<Point> {
	return sequenceOf(
		Point(point.x - 1, point.y),
		Point(point.x + 1, point.y),
		Point(point.x, point.y - 1),
		Point(point.x, point.y + 1)
	)
		.filter { isInGrid(it, maxPoint) }
		.toSet()
}

fun risk(grid: List<List<Int>>, point: Point): Int {
	val tileHeight = grid.size
	val tileWidth = grid.first().size
	return (grid[point.y % tileHeight][point.x % tileWidth] + point.x / tileWidth + point.y / tileHeight - 1) % 9 + 1
}

fun findLowestDistancePoint(points: Set<Point>, distances: List<IntArray>): Point? {
	var lowestDistancePoint: Point? = null
	var lowestDistance = Int.MAX_VALUE
	for (p in points) {
		val dist = distances[p.y][p.x]
		if (dist < lowestDistance) {
			lowestDistance = dist
			lowestDistancePoint = p
		}
	}
	return lowestDistancePoint
}

fun findRoute(grid: List<List<Int>>): Pair<List<Point>, Int> {
	val yRange = 0 until grid.size * 5
	val xLimit = grid[0].size * 5
	val xRange = 0 until xLimit
	val destination = Point(xRange.last, yRange.last)
	val distances = yRange.map { IntArray(xLimit) { Int.MAX_VALUE } }
	distances[0][0] = 0
	val previous: List<MutableList<Point?>> = yRange.map { MutableList<Point?>(xLimit) { null } }
	val settled = mutableSetOf<Point>()
	val unsettled = mutableSetOf(Point(0, 0))
	while (unsettled.isNotEmpty()) {
		val p = findLowestDistancePoint(unsettled, distances) ?: throw (IllegalStateException())
		unsettled.remove(p)
		neighbours(p, destination)
			.filter { !settled.contains(it) }
			.forEach {
				val dist = distances[p.y][p.x] + risk(grid, it)
				if (dist < distances[it.y][it.x]) {
					distances[it.y][it.x] = dist
					previous[it.y][it.x] = p
				}
				unsettled.add(it)
			}
		settled.add(p)
	}
	return Pair(
		(generateSequence(destination) { (previous[it.y][it.x]) }).toList().reversed(),
		distances.last().last()
	)
}

fun main() {
	val grid = generateSequence(::readLine).map { l ->
		l.map { it.digitToInt() }
	}.toList()
	val route: Pair<List<Point>, Int>
	val time = measureTimeMillis { route = findRoute(grid) }
	println(route)
	println(Duration.ofMillis(time))
}
