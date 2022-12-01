package aoc2022.d01

fun main() {
	println(findMaximum(parse(generateSequence(::readLine))))
}

fun parse(input: Sequence<String>): List<List<Int>> {
    return input.fold(listOf(listOf<Int>())) { list, line: String ->
        if (line.isEmpty()) {
            list.plusElement(listOf<Int>())
        } else {
            list.subList(0, list.size - 1).plusElement(list.last() + line.toInt())
        }
    }
}

fun findMaximum(caloriesList: List<List<Int>>): Int {
    return caloriesList.maxOf { it.sum() }
}
