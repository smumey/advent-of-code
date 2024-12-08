package aoc2022.d17

import aoc.Coordinate
import aoc.Direction
import kotlin.math.min

interface Rock {
    fun contains(x: Int, y: Int): Boolean

    fun boundary(direction: Direction): Int

    fun move(direction: Direction): Rock
}

data class HorizontalRock(val bottomLeft: Coordinate) : Rock {

    override fun contains(x: Int, y: Int): Boolean {
        return x in bottomLeft.x..bottomLeft.x + 3 && y == bottomLeft.y
    }

    override fun boundary(direction: Direction) =
        when (direction) {
            Direction.UP -> bottomLeft.y
            Direction.RIGHT -> bottomLeft.x + 3
            Direction.DOWN -> bottomLeft.y
            Direction.LEFT -> bottomLeft.x
        }

    override fun move(direction: Direction) = HorizontalRock(bottomLeft.moveMirrored(direction))
}

data class CrossRock(val bottomLeft: Coordinate) : Rock {
    override fun contains(x: Int, y: Int): Boolean {
        return when {
            x in bottomLeft.x..bottomLeft.x + 2 && y == bottomLeft.y + 1 -> true
            y in bottomLeft.y..bottomLeft.y + 2 && x == bottomLeft.x + 1 -> true
            else -> false
        }
    }

    override fun boundary(direction: Direction) =
        when (direction) {
            Direction.UP -> bottomLeft.y + 2
            Direction.RIGHT -> bottomLeft.x + 2
            Direction.DOWN -> bottomLeft.y
            Direction.LEFT -> bottomLeft.x
        }

    override fun move(direction: Direction) = CrossRock(bottomLeft.moveMirrored(direction))
}

data class CornerRock(val bottomLeft: Coordinate) : Rock {

    override fun contains(x: Int, y: Int): Boolean {
        return when {
            x in bottomLeft.x..bottomLeft.x + 1 && y == bottomLeft.y -> true
            x == bottomLeft.x + 2 && y in bottomLeft.y..bottomLeft.y + 2 -> true
            else -> false
        }
    }

    override fun boundary(direction: Direction) =
        when (direction) {
            Direction.UP -> bottomLeft.y + 2
            Direction.RIGHT -> bottomLeft.x + 2
            Direction.DOWN -> bottomLeft.y
            Direction.LEFT -> bottomLeft.x
        }

    override fun move(direction: Direction) = CornerRock(bottomLeft.moveMirrored(direction))
}

data class VerticalRock(val bottomLeft: Coordinate) : Rock {
    override fun contains(x: Int, y: Int): Boolean {
        return x == bottomLeft.x && y in bottomLeft.y..bottomLeft.y + 3
    }

    override fun boundary(direction: Direction) =
        when (direction) {
            Direction.UP -> bottomLeft.y + 3
            Direction.RIGHT -> bottomLeft.x
            Direction.DOWN -> bottomLeft.y
            Direction.LEFT -> bottomLeft.x
        }

    override fun move(direction: Direction) = VerticalRock(bottomLeft.moveMirrored(direction))
}

data class BoulderRock(val bottomLeft: Coordinate) : Rock {
    override fun contains(x: Int, y: Int): Boolean {
        return x in bottomLeft.x..bottomLeft.x + 1 && y in bottomLeft.y..bottomLeft.y + 1
    }

    override fun boundary(direction: Direction) =
        when (direction) {
            Direction.UP -> bottomLeft.y + 1
            Direction.RIGHT -> bottomLeft.x + 1
            Direction.DOWN -> bottomLeft.y
            Direction.LEFT -> bottomLeft.x
        }

    override fun move(direction: Direction) = BoulderRock(bottomLeft.moveMirrored(direction))
}

fun parse(line: String): List<Direction> {
    return line.map {
        when (it) {
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            else -> throw IllegalArgumentException()
        }
    }
}

class Cave(val winds: List<Direction>) {
    val right = 7
    val floors = mutableListOf<MutableList<Boolean>>()
    var windIndex = 0
    var rockIndex = 0
    var rockCount = 0

    fun nextWind(): Direction {
        val w = winds[windIndex]
        windIndex = (windIndex + 1) % winds.size
        return w
    }

    fun inbounds(rock: Rock): Boolean {
        return rock.boundary(Direction.LEFT) >= 0 &&
                rock.boundary(Direction.RIGHT) < right &&
                rock.boundary(Direction.DOWN) >= 0 &&
                checkFits(rock)
    }

    fun checkFits(rock: Rock): Boolean {
        return (rock.boundary(Direction.DOWN)..min(floors.size - 1, rock.boundary(Direction.UP))).all { y ->
            (rock.boundary(Direction.LEFT)..rock.boundary(Direction.RIGHT)).all { x ->
                !floors[y][x] || !rock.contains(x, y)
            }
        }
    }

    fun nextRock(): Rock {
        val bottomLeft = Coordinate(2, floors.size + 3)
        val rock = when (rockIndex) {
            0 -> HorizontalRock(bottomLeft)
            1 -> CrossRock(bottomLeft)
            2 -> CornerRock(bottomLeft)
            3 -> VerticalRock(bottomLeft)
            4 -> BoulderRock(bottomLeft)
            else -> throw IllegalStateException()
        }
        rockIndex = (rockIndex + 1) % 5
        return rock
    }

    fun processRock() {
        var rock = nextRock()
        while (true) {
            val wind = nextWind()
            val windRock = rock.move(wind)
            if (inbounds(windRock)) rock = windRock
            val downRock = rock.move(Direction.DOWN)
            if (inbounds(downRock)) rock = downRock
            else {
                break
            }
        }
        updateFloor(rock)
        rockCount++
    }

    private fun updateFloor(rock: Rock) {
        (rock.boundary(Direction.DOWN)..rock.boundary(Direction.UP)).forEach { y ->
            when (y) {
                floors.size -> floors.add(MutableList(right) { rock.contains(it, y) })
                in 0 until floors.size -> floors[y].forEachIndexed { x, filled ->
                    floors[y][x] = filled || rock.contains(x, y)
                }

                else -> throw IllegalStateException()
            }
        }
    }

    fun marker(): StateMarker {
        return StateMarker(windIndex, rockIndex, floors.takeLast(winds.size))
    }
}

fun getHeight(rockCount: Int, winds: List<Direction>): Int {
    val cave = Cave(winds)
    (0 until rockCount).forEach {
        cave.processRock()
    }
    return cave.floors.size
}

data class StateMarker(val windIndex: Int, val rockIndex: Int, val layers: List<List<Boolean>>)

fun getHeightLarge(rockCount: Long, winds: List<Direction>): Long {
    val cave = Cave(winds)
    repeat(100 * winds.size) {
        cave.processRock()
    }
    val stateMarker = cave.marker()
    val count = cave.rockCount
    val height = cave.floors.size
    var repeatMarker: StateMarker
    do {
        cave.processRock()
        if (cave.rockCount % 1000 == 0) {
            println("still looking for repeat after ${cave.rockCount}")
        }
    } while (cave.marker() != stateMarker)
    val repeatHeight = cave.floors.size
    val repeatCount = cave.rockCount
    val cycleSize = repeatCount - count
    val remainingRocks = rockCount - cave.rockCount
    val cycles = remainingRocks / cycleSize
    val remainder = remainingRocks % cycleSize
    repeat(remainder.toInt()) {
        cave.processRock()
    }
    return (repeatHeight - height) * cycles + cave.floors.size
}
