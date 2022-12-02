package aoc2022.d02

import aoc2022.d01.findSumOfTop3

enum class Choice(val points: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun score(opponentChoice: Choice): Int {
        return when ((3 + opponentChoice.ordinal - ordinal)%3) {
            0 -> points + 3
            1 -> points
            2 -> points + 6
            else -> 0
        }
    }
}

data class Round(val opponentChoice: Choice, val choice: Choice) {
    fun score() = choice.score(opponentChoice)
}

fun parse(line: String): Round {
    val (opponentLetter, myLetter) = line.split(" ")
    return Round(Choice.values()[opponentLetter[0]-'A'], Choice.values()[myLetter[0]-'X'])
}

fun evaluate(rounds: Sequence<Round>): Int {
    return rounds.sumOf(Round::score)
}

fun main() {
    println(evaluate(generateSequence(::readLine).map { parse(it) }))
}
