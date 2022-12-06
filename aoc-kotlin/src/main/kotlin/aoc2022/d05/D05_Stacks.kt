package aoc2022.d05

import java.util.Scanner
import readInput

fun parse(input: List<String>): Pair<StackState, List<Move>> {
	return Pair(
		parseInitialState(input.takeWhile { it.isNotEmpty() }.toList().reversed()),
		parseMoves(input.dropWhile { it.isNotEmpty() }.drop(1))
	)
}

data class StackState(val stacks: List<List<Char>>) {
	fun move(source: Int, target: Int): StackState {
		val stackState = StackState(
			stacks.mapIndexed { i, stack ->
				when (i) {
					source -> stack.subList(0, stack.size - 1)
					target -> stack + stacks[source].last()
					else -> stack
				}
			}
		)
		return stackState
	}

	fun moveBatch(move: Move): StackState {
		return StackState(
			stacks.mapIndexed { i, stack ->
				when (i) {
					move.source -> stack.take(stack.size - move.count)
					move.target -> stack + stacks[move.source].takeLast(move.count)
					else -> stack
				}
			}
		)

	}

	fun message(): String {
		return stacks.mapNotNull { it.lastOrNull() }.joinToString("")
	}
}

fun parseInitialState(input: List<String>): StackState {
	val numberOfStacks = input[0].trim().split(Regex("\\s+")).size
	val stacks = IntRange(0, numberOfStacks - 1).map { mutableListOf<Char>() }
	input.subList(1, input.size).fold(stacks) { _, line ->
		stacks.forEachIndexed { i, crate ->
			val crate = line.getOrNull(i * 4 + 1)
			if (crate != null && crate != ' ') {
				stacks[i].add(crate)
			}
		}
		stacks
	}
	return StackState(stacks)
}

data class Move(val source: Int, val target: Int, val count: Int) {
	fun apply(state: StackState): StackState {
		return IntRange(0, count - 1).fold(state) { state, _ -> state.move(source, target) }
	}
}

fun parseMoves(input: List<String>): List<Move> {
	return input.map {
		val scanner = Scanner(it).useDelimiter("\\s*(move|from|to)\\s*")
		val count = scanner.nextInt()
		val source = scanner.nextInt() - 1
		val target = scanner.nextInt() - 1
		Move(source, target, count)
	}.toList()
}

fun applyMoves(stackState: StackState, moves: List<Move>): StackState {
	return moves.fold(stackState) { state, move -> move.apply(state) }
}

fun applyBatchMoves(stackState: StackState, moves: List<Move>): StackState {
	return moves.fold(stackState) { state, move -> state.moveBatch(move) }
}

fun main() {
	var input = parse(readInput("aoc2022/5"))
	println(applyBatchMoves(input.first, input.second).message())
}