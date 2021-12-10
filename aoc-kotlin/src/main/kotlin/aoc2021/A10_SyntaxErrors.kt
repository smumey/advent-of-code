package aoc2021

private class Stack {
	private val content = mutableListOf<Char>()

	fun handle(char: Char): Boolean {
		return when (char) {
			'<' -> push('<')
			'>' -> popCheck('<')
			'[' -> push('[')
			']' -> popCheck('[')
			'{' -> push('{')
			'}' -> popCheck('{')
			'(' -> push('(')
			')' -> popCheck('(')
			else -> false
		}
	}

	fun push(char: Char): Boolean {
		content.add(char)
		return true
	}

	fun popCheck(char: Char): Boolean {
		return if (content.isEmpty()) false
		else content.removeLast() == char
	}
}

fun checkLine(line: CharArray): Char? {
	val stack = Stack()
	return line.map { Pair(it, stack.handle(it)) }
		.filter { !it.second }
		.map { it.first }
		.firstOrNull()
}

fun main() {
	println(
		generateSequence(::readLine).toList()
			.map(String::toCharArray)
			.map(::checkLine)
			.filterNotNull()
			.sumOf { it ->
				println(it)
				when (it) {
					')' -> 3
					']' -> 57
					'}' -> 1197
					'>' -> 25137
					else -> Int.MAX_VALUE
				}
			}
	)
}
