package aoc2022.d08

import aoc.readInput

enum class Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT
}

data class Coordinate(val x: Int, val y: Int)

data class TreePatch(val trees: List<Int>, val width: Int) {
	fun isInside(coord: Coordinate): Boolean {
		return coord.x in 0 until width && coord.y * width in 0 until trees.size
	}

	fun move(coord: Coordinate, direction: Direction): Coordinate? {
		val new = when (direction) {
			Direction.UP -> Coordinate(coord.x, coord.y - 1)
			Direction.RIGHT -> Coordinate(coord.x + 1, coord.y)
			Direction.DOWN -> Coordinate(coord.x, coord.y + 1)
			Direction.LEFT -> Coordinate(coord.x - 1, coord.y)
		}
		return if (isInside(new)) new else null
	}

	fun height(coord: Coordinate): Int {
		return trees[coord.y * width + coord.x]
	}

	fun interveningTrees(coord: Coordinate, direction: Direction): Sequence<Coordinate> {
		return generateSequence(coord) { coord -> move(coord, direction) }.drop(1)
	}

	fun countVisible(): Int {
		return trees.indices.map { coordinate(it) }.count {
			Direction.values().any { direction ->
				interveningTrees(it, direction).map { t -> height(t) }.all { h -> h < height(it) }
			}
		}
	}

	private fun coordinate(index: Int) = Coordinate(index % width, index / width)

	fun scenicScore(coord: Coordinate): Int {
		return Direction.values()
			.map { direction ->
				val trees = interveningTrees(coord, direction)
				var count = 0
				for (tree in trees) {
					count += 1
					if (height(tree) >= height(coord)) {
						break
					}
				}
				count
			}
			.reduce { s1, s2 -> s1 * s2 }
	}

	fun maxScenicScore(): Int {
		return trees.indices.map { it -> coordinate(it) }.maxOf { scenicScore(it) }
	}
}

fun parse(input: List<String>): TreePatch {
	val rows = input.map { it.trim().windowed(1).map(String::toInt) }
	return TreePatch(rows.flatten(), rows[0].size)
}

fun main() {
	println(parse(readInput("aoc2022/8")).maxScenicScore())
}
