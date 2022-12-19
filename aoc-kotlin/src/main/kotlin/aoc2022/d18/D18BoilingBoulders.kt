package aoc2022.d18

data class Coordinate3D(val x: Int, val y: Int, val z: Int)

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

fun surfaceArea(cubes: List<Cube>): Int {
	var faces = cubes.map { it.faces }
		.fold(List<Map<Coordinate3D, Int>>(3) { mapOf() }) { list, cubeFacesList ->
			list.mapIndexed { i, l ->
				val cubeFaces = cubeFacesList[i]
				val map = l.toMutableMap()
				cubeFaces.forEach {
					map.merge(it, 1, Int::plus)
				}
				map
			}
		}
	return faces.sumOf { it.filterValues { count -> count == 1 }.count() }
}
