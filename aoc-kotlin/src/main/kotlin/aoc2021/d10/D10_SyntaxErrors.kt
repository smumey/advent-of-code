package aoc2021.d10

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

	fun complete(): CharArray {
		return content.reversed()
			.map {
				when (it) {
					'<' -> '>'
					'[' -> ']'
					'{' -> '}'
					'(' -> ')'
					else -> '*'
				}
			}
			.toCharArray()
	}
}

fun checkLine(line: CharArray): Char? {
	val stack = Stack()
	return line.map { Pair(it, stack.handle(it)) }
		.filter { !it.second }
		.map { it.first }
		.firstOrNull()
}

fun complete(line: CharArray): CharArray {
	val stack = Stack()
	line.forEach(stack::handle)
	return stack.complete()
}

fun main() {
	val scores = generateSequence(::readLine).toList()
		.map(String::toCharArray)
		.filter { it -> checkLine(it) == null }
		.map(::complete)
//		.map{ it.joinToString("") }
		.map { chars ->
			chars.fold(0L) { s, c ->
				s * 5L + when (c) {
					')' -> 1L
					']' -> 2L
					'}' -> 3L
					'>' -> 4L
					else -> Long.MIN_VALUE
				}
			}
		}
		.sorted()
	println(scores)
	println(scores[scores.size / 2])
}
