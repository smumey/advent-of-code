package aoc2022.d11

import aoc.readInput
import java.util.function.LongUnaryOperator

typealias ItemsList = List<List<Long>>

data class Monkey(
	val index: Int,
	val operation: LongUnaryOperator,
	val divisor: Long,
	val trueTarget: Int,
	val falseTarget: Int
) {
	fun processItems(items: ItemsList, boredom: Long, modulo: Long): ItemsList {
		val ownItems = items[index]
		val newItems = ownItems.map { (inspect(it) / boredom) % modulo }
		return items.mapIndexed { i, monkeyItems ->
			(if (i == index) listOf() else monkeyItems) + newItems.filter { determineTarget(it) == i }
		}
	}

	fun determineTarget(item: Long) = if (item % divisor == 0L) trueTarget else falseTarget

	fun inspect(item: Long) = operation.applyAsLong(item)
}

data class State(val monkeys: List<Monkey>, val roundState: RoundState)

data class RoundState(val items: ItemsList, val inspectCounts: List<Int>)

fun parse(input: List<String>): State {
	val lines = input.iterator()
	val pairs = generateSequence { parseMonkey(lines) }.toList()

	return State(pairs.map { it.first }, RoundState(pairs.map { it.second }, pairs.map { 0 }))
}

fun parseMonkey(lines: Iterator<String>): Pair<Monkey, List<Long>>? {
	var index = -1
	while (lines.hasNext()) {
		val line = lines.next()
		if (line.isNotEmpty()) {
			index = line.substring("Monkey ".length, line.length - 1).toInt()
			break
		}
	}
	if (!lines.hasNext()) return null
	val itemLine = lines.next()
	val items = itemLine.substring("  Starting items: ".length).split(", ").map(String::toLong)
	val (xS, operatorString, yS) = lines.next().substring("  Operation: new = ".length).split(" ")
	val divisor = lines.next().substring("  Test: divisible by ".length).toLong()
	val trueMonkey = lines.next().substring("    If true: throw to monkey ".length).toInt()
	val falseMonkey = lines.next().substring("    If false: throw to monkey ".length).toInt()
	val operator = parseOperator(operatorString)
	val x = parseOperand(xS)
	val y = parseOperand(yS)

	return Pair(
		Monkey(
			index,
			{ item -> operator(x(item), y(item)) },
			divisor,
			trueMonkey,
			falseMonkey
		),
		items
	)
}

private fun parseOperator(operatorString: String): (Long, Long) -> Long {
	return when (operatorString) {
		"+" -> ({ i, j -> i + j })
		"*" -> ({ i, j -> i * j })
		else -> throw RuntimeException()
	}
}

private fun parseOperand(operandString: String): (Long) -> Long = when (operandString) {
	"old" -> ({ it })
	else -> {
		val value = operandString.toLong()
		({ value })
	}
}

fun executeRound(roundStateAtStart: RoundState, monkeys: List<Monkey>, divisor: Long, modulo: Long): RoundState {
	return monkeys.fold(roundStateAtStart) { roundState, monkey ->
		RoundState(
			monkey.processItems(roundState.items, divisor, modulo),
			roundState.inspectCounts.mapIndexed { i, count ->
				when (i) {
					monkey.index -> count + roundState.items[i].size
					else -> count
				}
			}
		)
	}
}

fun executeRounds(
	roundStateAtStart: RoundState,
	monkeys: List<Monkey>,
	divisor: Long,
	modulo: Long,
	rounds: Int,
): RoundState {
	return generateSequence(roundStateAtStart) { roundState -> executeRound(roundState, monkeys, divisor, modulo) }
		.take(rounds + 1)
		.last()
}

fun findActiveMonkeyScore(inspectCounts: List<Int>): Long {
	return inspectCounts.sortedDescending().take(2).fold(1L) { i, j -> i * j }
}

fun getDivisorProduct(monkeys: List<Monkey>): Long {
	return monkeys.map { it.divisor }.reduce { d1, d2 -> d1 * d2 }
}

fun main() {
	val state = parse(readInput("aoc2022/11"))
	println(
		findActiveMonkeyScore(
			executeRounds(
				state.roundState,
				state.monkeys,
				1,
				getDivisorProduct(state.monkeys),
				10000
			).inspectCounts
		)
	)
}
