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

data class LongCoordinate(val x: Long, val y: Long) {
	fun move(direction: Direction): LongCoordinate {
		return when (direction) {
			Direction.UP -> LongCoordinate(x, y + 1L)
			Direction.RIGHT -> LongCoordinate(x + 1L, y)
			Direction.DOWN -> LongCoordinate(x, y - 1L)
			Direction.LEFT -> LongCoordinate(x - 1L, y)
		}
	}
}

enum class Direction {
	UP, RIGHT, DOWN, LEFT
}
