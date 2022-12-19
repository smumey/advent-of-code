package aoc2022.d18

import aoc.Coordinate3D
import aoc.Direction3D

data class Cube(val coordinate: Coordinate3D) {
	val faces: List<Set<Coordinate3D>> = listOf(
		setOf(
			Coordinate3D(coordinate.x, coordinate.y, coordinate.z),
			Coordinate3D(coordinate.x, coordinate.y, coordinate.z + 1)
		),
		setOf(
			Coordinate3D(coordinate.x, coordinate.y, coordinate.z),
			Coordinate3D(coordinate.x, coordinate.y + 1, coordinate.z)
		),
		setOf(
			Coordinate3D(coordinate.x, coordinate.y, coordinate.z),
			Coordinate3D(coordinate.x + 1, coordinate.y, coordinate.z)
		),
	)
}

fun parse(line: String): Cube {
	val (x, y, z) = line.split(",").map(String::toInt)
	return Cube(Coordinate3D(x, y, z))
}

fun getSurfaceFaces(cubes: List<Cube>): List<Set<Coordinate3D>> {
	return cubes.map { it.faces }
		.fold(List<Map<Coordinate3D, Int>>(3) { mapOf() }) { list, cubeFacesList ->
			list.mapIndexed { i, l ->
				val cubeFaces = cubeFacesList[i]
				val map = l.toMutableMap()
				cubeFaces.forEach {
					map.merge(it, 1, Int::plus)
				}
				map
			}
		}.map { it.filterValues { count -> count == 1 }.keys }
}

fun surfaceArea(cubes: List<Cube>): Int {
	return getSurfaceFaces(cubes).sumOf { it.count() }
}

fun floodFill(grid: MutableList<MutableList<MutableList<Boolean>>>, cubes: List<Cube>) {

}

fun exteriorSurfaceArea(cubes: List<Cube>): Int {
	val surfaceFaces = getSurfaceFaces(cubes)
	val xMin = cubes.map { it.coordinate.x }.minOrNull()!! - 1
	val xMax = cubes.map { it.coordinate.x }.maxOrNull()!! + 1
	val yMin = cubes.map { it.coordinate.x }.minOrNull()!! - 1
	val yMax = cubes.map { it.coordinate.y }.maxOrNull()!! + 1
	val zMin = cubes.map { it.coordinate.z }.minOrNull()!! - 1
	val zMax = cubes.map { it.coordinate.z }.maxOrNull()!! + 1
	val grid = MutableList(zMax - zMin + 1) { MutableList(yMax - yMin + 1) { MutableList(xMax - xMin + 1) { false } } }
	fun isInGrid(coordinate: Coordinate3D) =
		coordinate.z in zMin..zMax && coordinate.y in yMin..yMax && coordinate.x in xMin..xMax

	val exteriorFaces = List(3) { mutableSetOf<Coordinate3D>() }

	fun setGrid(coordinate: Coordinate3D) {
		grid[coordinate.z - zMin][coordinate.y - yMin][coordinate.x - xMin] = true
		val cube = Cube(coordinate)
		cube.faces.forEachIndexed { i, set ->
			set.forEach { face ->
				if (surfaceFaces[i].contains(face)) exteriorFaces[i].add(face)
			}
		}
	}

	fun getGrid(coordinate: Coordinate3D) = grid[coordinate.z - zMin][coordinate.y - yMin][coordinate.x - xMin]

	val cubeCoordsSet = cubes.map { it.coordinate }.toSet()

	val queue = mutableListOf<Coordinate3D>(Coordinate3D(xMin, yMin, zMin))

	while (queue.isNotEmpty()) {
		val coordinate = queue.removeLast()
		if (coordinate !in cubeCoordsSet && !getGrid(coordinate)) {

			setGrid(coordinate)
			Direction3D.values()
				.map { coordinate.move(it) }
				.filter { isInGrid(it) }
				.forEach { queue.add(it) }
		}
	}

	return exteriorFaces.sumOf { it.count() }
}
