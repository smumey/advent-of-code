package aoc

data class Coordinate(val x: Int, val y: Int) {
    fun moveMirrored(direction: Direction): Coordinate {
        return when (direction) {
            Direction.UP -> Coordinate(x, y + 1)
            Direction.RIGHT -> Coordinate(x + 1, y)
            Direction.DOWN -> Coordinate(x, y - 1)
            Direction.LEFT -> Coordinate(x - 1, y)
        }
    }

    fun move(direction: Direction): Coordinate {
        return when (direction) {
            Direction.UP -> Coordinate(x, y - 1)
            Direction.RIGHT -> Coordinate(x + 1, y)
            Direction.DOWN -> Coordinate(x, y + 1)
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
    UP, RIGHT, DOWN, LEFT;

    fun opposite(): Direction {
        return Direction.entries.get((this.ordinal + 2) % 4)
    }
}

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    fun move(direction: Direction3D): Coordinate3D {
        return when (direction) {
            Direction3D.UP -> Coordinate3D(x, y + 1, z)
            Direction3D.RIGHT -> Coordinate3D(x + 1, y, z)
            Direction3D.DOWN -> Coordinate3D(x, y - 1, z)
            Direction3D.LEFT -> Coordinate3D(x - 1, y, z)
            Direction3D.IN -> Coordinate3D(x, y, z - 1)
            Direction3D.OUT -> Coordinate3D(x, y, z + 1)
        }
    }

}

enum class Direction3D {
    UP, RIGHT, DOWN, LEFT, IN, OUT
}
