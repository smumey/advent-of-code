package aoc2021

import java.util.*

private data class HeightPoint(val x: Int, val y: Int, val height: Int) {
	fun neighbours(map: HeightMap): Set<HeightPoint> {
		return ((y - 1..y + 1 step 2)
			.filter { 0 <= it && it < map.mapHeight }
			.map { map.getPoint(x, it) } +
				(x - 1..x + 1 step 2)
					.filter { 0 <= it && it < map.mapWidth }
					.map { map.getPoint(it, y) }).toSet()
	}
}

private data class HeightMap(val heights: List<Int>, val mapWidth: Int) {
	val mapHeight = heights.size / mapWidth

	fun findLows(points: List<HeightPoint>): List<HeightPoint> {
		var lows = emptyList<HeightPoint>()
		var candidates = points
		while (candidates.isNotEmpty()) {
			val neighbours = candidates[0].neighbours(this)
			if (neighbours.all { it.height > candidates[0].height }) {
				lows += candidates[0]
				candidates = candidates.subList(1, candidates.size)
					.filter { !neighbours.contains(it) }
			} else {
				candidates = candidates.subList(1, candidates.size)
			}
		}
		return lows
	}

	private fun sortedPoints(): List<HeightPoint> {
		return heights.mapIndexed { i, h -> HeightPoint(i % mapWidth, i / mapWidth, h) }
			.sortedWith { p1, p2 -> p1.height.compareTo(p2.height) }
	}

	fun findLows(): List<HeightPoint> {
		return findLows(sortedPoints())
	}

	fun getPoint(x: Int, y: Int): HeightPoint {
		return HeightPoint(x, y, heights[y * mapWidth + x])
	}
}

fun main() {
	val cells = generateSequence(::readLine)
		.map { it.toCharArray().map { c -> c - '0' } }
		.toList()
	println(
		HeightMap(cells.flatten(), cells[0].size).findLows()
			.sumOf { it.height + 1 }
	)
}
