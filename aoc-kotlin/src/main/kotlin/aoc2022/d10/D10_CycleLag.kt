package aoc2022.d10

import aoc.readInput
import java.util.Scanner
import java.util.function.IntUnaryOperator

const val INITIAL_VALUE = 1
val CYCLE_SAMPLE = 20..220 step 40

enum class Operation(val duration: Int) {
	NOOP(1),
	ADDX(2)
}

data class Instruction(val operation: Operation, val operator: IntUnaryOperator)

fun parse(line: String): Instruction {
	val scanner = Scanner(line)
	return when (scanner.next()) {
		"noop" -> Instruction(Operation.NOOP, IntUnaryOperator.identity())
		"addx" -> {
			val arg = scanner.nextInt()
			Instruction(Operation.ADDX) { it + arg }
		}

		else -> throw RuntimeException()
	}
}

fun calculateRegisterValues(instructions: List<Instruction>): List<Int> {
	return instructions.fold(listOf<Int>(1)) { registerValues, instruction ->
		registerValues +
				List(instruction.operation.duration - 1) { registerValues.last() } +
				instruction.operator.applyAsInt(registerValues.last())
	}
}

fun calculateSignalStrengthSum(registerValues: List<Int>): Int {
	return CYCLE_SAMPLE.sumOf { registerValues[it - 1] * it }
}

fun render(registerValues: List<Int>): String {
	val list: List<String> = (0 until 6).fold(listOf<String>()) { lines, row ->
		lines + (0 until 40).fold("") { line, column ->
			val index = registerValues[row * 40 + column]
			line + if (column in index - 1..index + 1) '#' else '.'
		}
	}
	return list.joinToString("") { it + '\n' }
}

fun main() {
	println(render(calculateRegisterValues(readInput("aoc2022/10").map(::parse))))
}
