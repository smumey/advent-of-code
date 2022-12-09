package aoc2022.d09

import aoc.Coordinate
import aoc.Direction
import aoc.readInput
import java.util.Scanner
import kotlin.math.abs

fun signum(x: Int) = when {
	x > 0 -> 1; x == 0 -> 0; else -> -1
}

fun follow(head: Coordinate, tail: Coordinate): Coordinate {
	val deltaX = head.x - tail.x
	val deltaY = head.y - tail.y
	return if (abs(deltaX) <= 1 && abs(deltaY) <= 1) tail
	else Coordinate(tail.x + signum(deltaX), tail.y + signum(deltaY))
}

data class MoveSteps(val direction: Direction, val steps: Int)

fun parse(line: String): MoveSteps {
	val scanner = Scanner(line)
	val direction = when (scanner.next()) {
		"U" -> Direction.UP
		"R" -> Direction.RIGHT
		"D" -> Direction.DOWN
		"L" -> Direction.LEFT
		else -> throw RuntimeException()
	}
	return MoveSteps(direction, scanner.nextInt())
}

fun countTailPositions(moveSteps: List<MoveSteps>, tailLength: Int = 1): Int {
	val initialCoord = Coordinate(0, 0)
	val headPositions = moveSteps.fold(listOf(initialCoord)) { coords, moveStep ->
		(0 until moveStep.steps).fold(coords) { stepCoords, _ ->
			stepCoords + stepCoords.last().move(moveStep.direction)
		}
	}
	val tailPositions = (0 until tailLength).fold(headPositions) { parentCoords, _ ->
		parentCoords.fold(listOf(initialCoord)) { childCoords, parentCoord ->
			childCoords + follow(parentCoord, childCoords.last())
		}
	}
	return tailPositions.toSet().size
}

fun main() {
	println(countTailPositions(readInput("aoc2022/9").map(::parse), 9))
}
