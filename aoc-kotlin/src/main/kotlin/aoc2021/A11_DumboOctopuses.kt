package aoc2021

private data class OctopusGrid(val rows: List<List<Int>>, val step: Int, val flashed: Int) {
	val gridHeight: Int = rows.size
	val gridWidth = rows[0].size

	fun trigger(cell: Pair<Int, Int>): OctopusGrid {
		val (j, i) = cell
		val grid = this.updateGrid(Pair(j, i), rows[j][i] + 1)
		return if (grid.rows[j][i] == 10) {
			this.neighbours(cell).fold(grid) { g, neighbour ->
				g.trigger(neighbour)
			}
		} else grid
	}

	fun beginStep(): OctopusGrid {
		return OctopusGrid(this.rows.map { r ->
			r.map { c -> if (c > 9) 0 else c }
		}, step + 1,0)
	}

	fun updateGrid(cell: Pair<Int, Int>, value: Int): OctopusGrid {
		val (j, i) = cell
		return OctopusGrid(
			rows.subList(0, j)
				.plusElement(rows[j].subList(0, i) + value + rows[j].subList(i + 1, rows[j].size))
				.plus(rows.subList(j + 1, rows.size)),
			this.step,
			if (value == 10) flashed + 1 else flashed
		)
	}

	fun advance(): OctopusGrid {
		val grid = beginStep()
		return grid.rows.foldIndexed(grid) { j, g, row ->
			row.foldIndexed(g) { i, h, _ ->
				h.trigger(Pair(j, i))
			}
		}
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
}

fun main() {
	val grid = OctopusGrid(
		generateSequence(::readLine)
			.map { it.toCharArray().map { c -> c - '0' }.toList() }
			.toList(),
		0,
		0
	)
	val final =
		generateSequence(grid, OctopusGrid::advance)
			.first { g -> g.flashed == g.gridHeight * g.gridWidth }
	println(final.step)
}
