package aoc2021.d02

enum class Direction {
	UP, DOWN, FORWARD
}

fun parseMove(line: String): Move {
	val (dir, scal) = line.split(" ")
	return Move(Direction.valueOf(dir.uppercase()), scal.toInt())
}

data class Move(val direction: Direction, val scalar: Int)

data class Position constructor(val horizontal: Int, val depth: Int, val aim: Int) {
	fun apply(move: Move): Position {
		return when (move.direction) {
			Direction.UP -> Position(horizontal, depth, aim - move.scalar)
			Direction.DOWN -> Position(horizontal, depth, aim + move.scalar)
			Direction.FORWARD -> Position(horizontal + move.scalar, depth + aim * move.scalar, aim)
		}
	}

	fun product(): Int {
		return depth * horizontal;
	}
}

fun main() {
	println(
		generateSequence(::readLine)
			.fold(
				Position(0, 0, 0),
				{ pos: Position, line: String -> pos.apply(parseMove(line)) }
			)
			.product()
	)
}
