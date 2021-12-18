package aoc2021.d17

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int)

val START = Point(0, 0)
val TARGET_RE = """
	^target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)$
	""".trim().toRegex()

private fun signum(i: Int): Int = if (i < 0) -1 else if (i > 0) 1 else 0

fun parseTarget(line: String): Pair<IntRange, IntRange> {
	val match = TARGET_RE.find(line) ?: throw IllegalArgumentException("input not a target")
	val (x1, x2, y1, y2) = match.groupValues.takeLast(4).map(String::toInt)
	return Pair(x1..x2, y1..y2)
}

fun yPosition(velY: Int, t: Int): Int {
	return if (velY >= t) {
		((velY * (velY + 1)) - (velY - t) * (velY - t + 1)) / 2
	} else if (velY >= 0) {
		((velY * (velY + 1)) - (t - velY) * (t - velY + 1)) / 2
	} else {
		-((t - 1 - velY) * (t - velY) - (-1 - velY) * -velY) / 2
	}
}

fun xPosition(velX: Int, t: Int): Int {
	val vel = abs(velX)
	return signum(velX) * if (t <= vel) {
		((vel * (vel + 1)) - (vel - t) * (vel - t + 1)) / 2
	} else {
		(vel * (vel + 1)) / 2
	}
}

fun possibleVelX(targetPoint: Point): IntRange {
	val absX = abs(targetPoint.x)
	val absMin = max(sqrt(2 * absX.toDouble()).toInt() - 1, 0)
	return if (targetPoint.x >= 0) absMin..absX else -absX..-absMin
}

fun possibleVelY(targetPoint: Point): IntRange {
	return -abs(targetPoint.y)..abs(targetPoint.y)
}

fun possibleT(targetPoint: Point, possibleVelX: IntRange): List<Pair<Int, Int>> {
	return possibleVelX.flatMap { velX ->
		(0..velX).takeWhile { abs(xPosition(velX, it)) <= abs(targetPoint.x) }
			.mapNotNull {
				if (xPosition(velX, it) == targetPoint.x) Pair(it, velX) else null
			}
	}
}

fun findTrajectories(targetPoint: Point): List<Triple<Int, Int, Int>> {
	return possibleT(targetPoint, possibleVelX(targetPoint)).flatMap { tVelX ->
		possibleVelY(targetPoint).mapNotNull { velY ->
			if (targetPoint.y == yPosition(velY, tVelX.first)) {
				Triple(tVelX.second, velY, tVelX.first)
			} else if (tVelX.first == abs(tVelX.second)) {
				var t = tVelX.second
				while (yPosition(velY, t) > targetPoint.y) {
					t += 1
				}
				if (yPosition(velY, t) == targetPoint.y) Triple(tVelX.second, velY, t) else null
			} else {
				null
			}
		}
	}
}

fun findMaximum(trajectory: Triple<Int, Int, Int>): Int {
	return if (trajectory.second <= 0) 0
	else if (trajectory.second > trajectory.third) yPosition(trajectory.second, trajectory.third)
	else yPosition(trajectory.second, trajectory.second)
}

fun findMaximumOverTarget(target: Pair<IntRange, IntRange>): Int? {
	return target.second.flatMap { y ->
		target.first.flatMap { x ->
			val trajectories = findTrajectories(Point(x, y))
			println(
				"target: ${Point(x, y)} trajectories: ${trajectories.map { Pair(it, findMaximum(it)) }}"
			)
			trajectories.map(::findMaximum)
		}
	}.maxOrNull()
}

fun findDistinctTrajectoriesOverTarget(target: Pair<IntRange, IntRange>): Set<Pair<Int, Int>> {
	return target.second.flatMap { y ->
		target.first.flatMap { x ->
			val trajectories = findTrajectories(Point(x, y))
			trajectories
		}
	}
		.map { t -> Pair(t.first, t.second) }
		.toSet()
}

fun main() {
	println(findDistinctTrajectoriesOverTarget(parseTarget(readLine() ?: "")).size)
}
