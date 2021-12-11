package aoc2021

private data class Octopus(val energy: Int)

private data class OctopusGrid(val rows: List<IntArray>) {
	private val gridHeight: Int = rows.size
	private val gridWidth = rows[0].size
	var flashCount = 0

	fun trigger(cell: Pair<Int, Int>) {
		val j = cell.first
		val i = cell.second
		rows[j][i] += 1
		if (rows[j][i] == 10) {
			flashCount++
			neighbours(cell).forEach(this::trigger)
		}
	}

	fun advance() {
		flashCount = 0
		rows.forEachIndexed { j, r ->
			r.forEachIndexed { i, _ -> trigger(Pair(j, i)) }
		}
		rows.forEach { r -> r.forEachIndexed { i, _ -> if (r[i] > 9) r[i] = 0 } }
	}

	fun neighbours(cell: Pair<Int, Int>): List<Pair<Int, Int>> {
		val j = cell.first
		val i = cell.second
		return ((j - 1)..(j + 1))
			.filter { nj -> nj in 0 until gridHeight }
			.flatMap { nj ->
				((i - 1)..(i + 1))
					.filter { ni -> ni in 0 until gridWidth && !(nj == j && ni == i) }
					.map { ni -> Pair(nj, ni) }
			}.toList()
	}

	fun repeatUntilSync(): Int {
		var step = 0
		while (flashCount != gridWidth * gridHeight) {
			advance()
			step += 1
		}
		return step
	}

	override fun toString(): String {
		return rows.joinToString("\n", transform = IntArray::contentToString)
	}

}

fun main() {
	val grid = OctopusGrid(
		generateSequence(::readLine)
			.map { it.toCharArray().map { c -> c - '0' }.toIntArray() }
			.toList()
	)
	println(grid.repeatUntilSync())
}
