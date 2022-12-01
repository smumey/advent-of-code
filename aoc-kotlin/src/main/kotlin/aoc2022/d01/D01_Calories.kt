package aoc2022.d01

fun main() {
	println(findSumOfTop3(parse(generateSequence(::readLine))))
}

fun parse(input: Sequence<String>): List<List<Int>> {
    return input.fold(listOf(listOf())) { list, line: String ->
        if (line.isEmpty()) {
            list.plusElement(listOf())
        } else {
            list.subList(0, list.size - 1).plusElement(list.last() + line.toInt())
        }
    }
}

fun findMaximum(caloriesList: List<List<Int>>): Int {
    return caloriesList.maxOf { it.sum() }
}

fun findSumOfTop3(caloriesList: List<List<Int>>): Int {
    return caloriesList.asSequence()
        .map { it.sum() }
        .sortedDescending()
        .take(3)
        .sum()
}
