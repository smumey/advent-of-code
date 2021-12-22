package aoc2021.d21

fun nextPos(pos: Int, die: Iterator<Int>): Int {
	val rollSum = die.next() + die.next() + die.next()
	return (pos - 1 + rollSum) % 10 + 1
}

fun calculateProduct(initialPositions: List<Int>): Int {
	val die = generateSequence(1) { it % 100 + 1 }.iterator()

	val states = generateSequence(Triple(initialPositions, initialPositions.map { 0 }, 0)) {
		val (positions, scores, turn) = it
		val p = turn % positions.size
		val nextPos = nextPos(positions[p], die)
		val nextScore = scores[p] + nextPos
		if (nextScore < 1000) {
			Triple(
				positions.mapIndexed { i, pos -> if (i == p) nextPos else pos },
				scores.mapIndexed { i, s -> if (i == p) s + nextPos else s },
				turn + 1
			)
		} else null
	}.toList()
	val last = states.last()
	val (positions, scores, turn) = last
	return scores[(turn - 1) % positions.size] * 3 * (turn + 1)
}

fun main() {
	println(calculateProduct(listOf(9, 3)))
}
