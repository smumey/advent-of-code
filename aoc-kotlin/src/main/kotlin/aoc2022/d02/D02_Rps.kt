package aoc2022.d02

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

enum class Strategy {
    LOSE, DRAW, WIN
}

data class Round(val opponentChoice: Choice, val choice: Choice) {
    fun score() = choice.score(opponentChoice)
}

fun parse(line: String): Round {
    val (opponentLetter, myLetter) = line.split(" ")
    return Round(Choice.values()[opponentLetter[0]-'A'], Choice.values()[myLetter[0]-'X'])
}

fun evaluateRounds(rounds: Sequence<Round>): Int {
    return rounds.sumOf(Round::score)
}

data class Plan(val opponentChoice: Choice, val strategy: Strategy)  {
    fun score() = Choice.values()[(opponentChoice.ordinal + strategy.ordinal)%3].score(opponentChoice)
}

fun parsePlan(line: String): Plan {
    val (opponentLetter, myLetter) = line.split(" ")
    return Plan(Choice.values()[opponentLetter[0]-'A'], Strategy.values()[(myLetter[0]-'X' + 2) % 3])
}
fun evaluatePlans(plans: Sequence<Plan>): Int {
    return plans.sumOf(Plan::score)
}

fun main() {
    println(evaluatePlans(generateSequence(::readLine).map { parsePlan(it) }))
}
