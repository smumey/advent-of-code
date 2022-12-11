package aoc2022.d11

import aoc.readInput
import java.util.function.IntUnaryOperator

typealias ItemsList = List<List<Int>>

data class Monkey(val index: Int, val operation: IntUnaryOperator, val test: IntUnaryOperator) {
	fun processItems(items: ItemsList): ItemsList {
		val ownItems = items[index]
		val newItems = ownItems.map { it -> growBored(inspect(it)) }
		return items.mapIndexed { i, monkeyItems ->
			(if (i == index) listOf<Int>() else monkeyItems) + newItems.filter { determineTarget(it) == i }
		}
	}

	fun determineTarget(item: Int) = test.applyAsInt(item)

	fun inspect(item: Int) = operation.applyAsInt(item)
	fun growBored(item: Int) = item / 3
}

data class State(val monkeys: List<Monkey>, val roundState: RoundState)

data class RoundState(val items: ItemsList, val inspectCounts: List<Int>)

fun parse(input: List<String>): State {
	val lines = input.iterator()
	val pairs = generateSequence { parseMonkey(lines) }.toList()

	return State(pairs.map { it.first }, RoundState(pairs.map { it.second }, pairs.map { 0 }))
}

fun parseMonkey(lines: Iterator<String>): Pair<Monkey, List<Int>>? {
	var index = -1
	while (lines.hasNext()) {
		var line = lines.next()
		if (line.isNotEmpty()) {
			index = line.substring("Monkey ".length, line.length - 1).toInt()
			break
		}
	}
	if (!lines.hasNext()) return null
	val itemLine = lines.next()
	val items = itemLine.substring("  Starting items: ".length).split(", ").map(String::toInt)
	val (xS, operatorString, yS) = lines.next().substring("  Operation: new = ".length).split(" ")
	val divisor = lines.next().substring("  Test: divisible by ".length).toInt()
	val trueMonkey = lines.next().substring("    If true: throw to monkey ".length).toInt()
	val falseMonkey = lines.next().substring("    If false: throw to monkey ".length).toInt()
	val operator = parseOperator(operatorString)
	val x = parseOperand(xS)
	val y = parseOperand(yS)

	return Pair(
		Monkey(
			index,
			{ item -> operator(x(item), y(item)) },
			{ item -> if (item % divisor == 0) trueMonkey else falseMonkey }
		),
		items
	)
}

private fun parseOperator(operatorString: String): (Int, Int) -> Int {
	return when (operatorString) {
		"+" -> ({ i, j -> i + j })
		"*" -> ({ i, j -> i * j })
		else -> throw RuntimeException()
	}
}

private fun parseOperand(operandString: String): (Int) -> Int = when (operandString) {
	"old" -> ({ it })
	else -> {
		val value = operandString.toInt()
		({ value })
	}
}

fun executeRound(roundStateAtStart: RoundState, monkeys: List<Monkey>): RoundState {
	return monkeys.fold(roundStateAtStart) { roundState, monkey ->
		RoundState(
			monkey.processItems(roundState.items),
			roundState.inspectCounts.mapIndexed { i, count ->
				when (i) {
					monkey.index -> count + roundState.items[i].size
					else -> count
				}
			}
		)
	}
}

fun executeRounds(roundStateAtStart: RoundState, monkeys: List<Monkey>, rounds: Int): RoundState {
	return generateSequence(roundStateAtStart) { roundState -> executeRound(roundState, monkeys) }
		.take(rounds + 1)
		.last()
}

fun findActiveMonkeyScore(inspectCounts: List<Int>): Int {
	return inspectCounts.sortedDescending().take(2).reduce { i, j -> i * j }
}

fun main() {
	val state = parse(readInput("aoc2022/11"))
	println(findActiveMonkeyScore(executeRounds(state.roundState, state.monkeys, 20).inspectCounts))

}
