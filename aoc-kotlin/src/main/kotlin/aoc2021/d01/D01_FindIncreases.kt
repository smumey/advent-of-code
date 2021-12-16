package aoc2021.d01

fun main() {
	println(
		generateSequence(::readLine)
			.map(String::toInt)
			.fold(
				intArrayOf(0, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
			) { acc, i: Int ->
				if (i > acc[1]) intArrayOf(acc[0] + 1, acc[2], acc[3], i) else intArrayOf(
					acc[0],
					acc[2],
					acc[3],
					i
				)
			}[0]
	)
}
