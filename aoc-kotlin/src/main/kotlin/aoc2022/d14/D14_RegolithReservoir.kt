package aoc2022.d14

import aoc.Coordinate
import aoc.Direction
import aoc.readInput
import kotlin.math.max
import kotlin.math.min

val sandSource = Coordinate(500, 0)

fun parse(line: String): List<Coordinate> {
    return line.split(" -> ")
        .map {
            val (x, y) = it.split(",").map(String::toInt)
            Coordinate(x, y)
        }
}

fun filled(rockCoordinates: List<Coordinate>): Set<Coordinate> {
    val rockLines = rockCoordinates.windowed(2)
    return rockLines.fold(setOf<Coordinate>()) { set, pair ->
        val c1 = pair[0]
        val c2 = pair[1]
        set + when {
            c1.x == c2.x -> (min(c1.y, c2.y)..max(c1.y, c2.y)).map { Coordinate(c1.x, it) }
            c1.y == c2.y -> (min(c1.x, c2.x)..max(c1.x, c2.x)).map { Coordinate(it, c1.y) }
            else -> throw IllegalArgumentException()
        }
    }
}

fun maxDepth(filled: Set<Coordinate>): Int {
    return filled.maxOf { it.y }
}

data class SandState(
    val maxDepth: Int,
    val filled: Set<Coordinate>,
    val sandCoordinate: Coordinate,
    val sandRestCount: Int
) {
    fun stepVoid(): SandState? {
        if (sandCoordinate.y >= maxDepth) return null
        val nextSandCoordinate =
            listOf(listOf(Direction.UP), listOf(Direction.UP, Direction.LEFT), listOf(Direction.UP, Direction.RIGHT))
                .map { it.fold(sandCoordinate) { sand, dir -> sand.moveFlip(dir) } }
                .find { !filled.contains(it) }
        return if (nextSandCoordinate == null) {
            SandState(maxDepth, filled + sandCoordinate, sandSource, sandRestCount + 1)
        } else {
            SandState(maxDepth, filled, nextSandCoordinate, sandRestCount)
        }
    }

    fun stepInfiniteFloor(): SandState? {
        if (filled.contains(sandSource)) return null
        val nextSandCoordinate =
            listOf(listOf(Direction.UP), listOf(Direction.UP, Direction.LEFT), listOf(Direction.UP, Direction.RIGHT))
                .map { it.fold(sandCoordinate) { sand, dir -> sand.moveFlip(dir) } }
                .find { !(filled.contains(it)) && it.y < maxDepth + 2 }
        return if (nextSandCoordinate == null) {
            SandState(maxDepth, filled + sandCoordinate, sandSource, sandRestCount + 1)
        } else {
            SandState(maxDepth, filled, nextSandCoordinate, sandRestCount)
        }
    }
}

fun main() {
    val rockCoordinates = readInput("aoc2022/14").map(::parse).flatMap(::filled).toSet()
    val initialState = SandState(maxDepth(rockCoordinates), rockCoordinates, sandSource, 0)
    println(generateSequence(initialState) { it.stepInfiniteFloor() }.last().sandRestCount)
}
