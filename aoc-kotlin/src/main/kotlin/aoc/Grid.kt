package aoc

data class Coordinate(val x: Int, val y: Int) {
	fun move(direction: Direction): Coordinate {
		return when (direction) {
			Direction.UP -> Coordinate(x, y + 1)
			Direction.RIGHT -> Coordinate(x + 1, y)
			Direction.DOWN -> Coordinate(x, y - 1)
			Direction.LEFT -> Coordinate(x - 1, y)
		}
	}
}

enum class Direction {
	UP, RIGHT, DOWN, LEFT
}
