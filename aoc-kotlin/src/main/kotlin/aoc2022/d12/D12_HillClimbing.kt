package aoc2022.d12

import aoc.Coordinate
import aoc.Direction
import aoc.readInput

fun infinityAdd(x: Int, y: Int): Int {
	return when (x) {
		Int.MAX_VALUE -> Int.MAX_VALUE
		else -> x + y
	}
}

data class Edge(val from: Coordinate, val to: Coordinate)

data class TopoMap(val rows: List<String>) {
	fun width() = rows[0].length

	fun find(height: Char): Coordinate? {
		return rows.indices.flatMap { y ->
			rows[y].indices.map { x -> Coordinate(x, y) }
		}.find { value(it) == height }
	}

	fun inbounds(coordinate: Coordinate): Boolean {
		return coordinate.y in rows.indices && coordinate.x in 0 until width()
	}

	fun neighbours(coordinate: Coordinate): List<Coordinate> {
		val neigbours = Direction.values()
			.map { coordinate.move(it) }
			.filter { inbounds(it) }
			.filter { height(it) - height(coordinate) <= 1 }
		return neigbours
	}

	fun value(coordinate: Coordinate) = rows[coordinate.y][coordinate.x]

	fun height(coordinate: Coordinate): Char {
		return when (val value = value(coordinate)) {
			'S' -> 'a'
			'E' -> 'z'
			else -> value
		}
	}

	fun findShortestPath(start: Coordinate, end: Coordinate): List<Coordinate> {
		val dist = mutableMapOf<Coordinate, Int>()
		val prev = mutableMapOf<Coordinate, Coordinate>()
		val queue = rows.indices.flatMap { y ->
			rows[y].indices.map { x -> Coordinate(x, y) }
		}.toMutableList()

		fun getDistance(coordinate: Coordinate) = dist.getOrDefault(coordinate, Int.MAX_VALUE)
		dist[start] = 0
		while (queue.isNotEmpty()) {
			queue.sortByDescending { getDistance(it) }
			val coord = queue.removeLast()
			neighbours(coord).filter { queue.contains(it) }.forEach { it ->
				val alt = infinityAdd(getDistance(coord), 1)
				if (alt < getDistance(it)) {
					dist[it] = alt
					prev[it] = coord
				}
			}
		}
		return generateSequence(end) { prev[it] }.toList().dropLast(1).reversed()
	}

}

fun parse(lines: List<String>): TopoMap {
	return TopoMap(lines)
}

fun main() {
	val topoMap = parse(readInput("aoc2022/12"))
	val start = topoMap.find('S')
	val end = topoMap.find('E')
	if (start != null && end != null) {
		println(topoMap.findShortestPath(start, end).size)
	}
}
