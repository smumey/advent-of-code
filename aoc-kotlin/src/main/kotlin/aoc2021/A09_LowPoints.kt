package aoc2021

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

	fun findBasins(points: List<HeightPoint>): Set<Set<HeightPoint>> {
		val basins: MutableSet<Set<HeightPoint>> = mutableSetOf()
		var candidates = points.filter { it.height < 9 }.toMutableList()
		while (candidates.isNotEmpty()) {
			var basin = setOf(candidates[0])
			val basinCandidates = mutableListOf(candidates[0])
			while (basinCandidates.isNotEmpty()) {
				var point = basinCandidates.removeAt(0)
				val adds = getAdds(point, candidates, basin)
				basinCandidates.addAll(adds)
				basin += adds
			}
			basins.add(basin)
			candidates -= basin
		}
		return basins
	}

	private fun getAdds(
		point: HeightPoint,
		candidates: MutableList<HeightPoint>,
		basin: Set<HeightPoint>
	) = point.neighbours(this)
		.filter { it.height < 9 && candidates.contains(it) && !basin.contains(it) }

	private fun sortedPoints(): List<HeightPoint> {
		return heights.mapIndexed { i, h -> HeightPoint(i % mapWidth, i / mapWidth, h) }
			.sortedWith { p1, p2 -> p1.height.compareTo(p2.height) }
	}

	fun findBasins(): Set<Set<HeightPoint>> {
		return findBasins(sortedPoints())
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
		HeightMap(cells.flatten(), cells[0].size).findBasins()
			.map { it.size }
			.sortedDescending()
			.take(3)
			.fold(1) { p, b -> p * b }
	)
}
