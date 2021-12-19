package aoc2021.d18

interface Number {
	fun explodeValue(): Long
	fun magnitude(): Long
	fun deepCopy(): Number
}

data class RegularNumber(var value: Long) : Number {
	fun split(): Number {
		if (value > 9) {
			return SnailfishNumber(RegularNumber(value / 2), RegularNumber(value - value / 2))
		}
		return this
	}

	override fun explodeValue(): Long {
		return value
	}

	override fun toString(): String {
		return value.toString()
	}

	override fun magnitude(): Long = value

	override fun deepCopy(): Number {
		return RegularNumber(value)
	}
}

data class SnailfishNumber(var left: Number, var right: Number) : Number {
	operator fun plus(other: SnailfishNumber): SnailfishNumber {
		val sum = this.add(other)
		sum.reduce()
		return sum
	}

	fun add(other: SnailfishNumber): SnailfishNumber {
		return SnailfishNumber(this.deepCopy(), other.deepCopy())
	}

	override fun deepCopy(): Number {
		return SnailfishNumber(left.deepCopy(), right.deepCopy())
	}

	private fun swap(from: Number, to: Number) {
		if (left === from) left = to
		else if (right === from) right = to
		else throw IllegalArgumentException("from $from not found in $this")
	}

	private fun nextRight(startQueue: List<Number>): RegularNumber? {
		val queue = startQueue.toMutableList()
		while (queue.isNotEmpty()) {
			when (val n = queue.removeLast()) {
				is RegularNumber -> return n
				is SnailfishNumber -> {
					queue.add(n.right)
					queue.add(n.left)
				}
			}
		}
		return null
	}

	fun splitTree(): Boolean {
		val queue = mutableListOf(Pair<SnailfishNumber?, Number>(null, this))
		while (queue.isNotEmpty()) {
			val (parent, number) = queue.removeLast()
			when (number) {
				is RegularNumber -> {
					val n = number.split()
					if (n is SnailfishNumber) {
						parent?.swap(number, n)
						return true
					}
				}
				is SnailfishNumber -> {
					queue.add(Pair(number, number.right))
					queue.add(Pair(number, number.left))
				}
			}
		}
		return false
	}

	fun explode(): Boolean {
		var lastLeft: RegularNumber? = null
		val queue = mutableListOf(Triple<SnailfishNumber?, Number, Int>(null, this, 0))
		while (queue.isNotEmpty()) {
			val (parent, number, depth) = queue.removeLast()
			if (depth == 4 && number is SnailfishNumber) {
				parent?.swap(number, RegularNumber(0))
				lastLeft?.let { it.value += number.left.explodeValue() }
				nextRight(queue.map { n -> n.second })?.let { it.value += number.right.explodeValue() }
				return true
			}
			when (number) {
				is RegularNumber -> lastLeft = number
				is SnailfishNumber -> {
					queue.add(Triple(number, number.right, depth + 1))
					queue.add(Triple(number, number.left, depth + 1))
				}
			}
		}
		return false
	}

	fun reduce() {
		if (explode()) reduce()
		if (splitTree()) reduce()
	}

	override fun explodeValue(): Long {
		return 0L
	}

	override fun magnitude(): Long {
		return 3 * left.magnitude() + 2 * right.magnitude()
	}

	override fun toString(): String {
		return "[$left,$right]"
	}
}

val digits = "-0123456789".toSet()

fun parseSnailFishNumber(string: String): Pair<SnailfishNumber, String> {
	val comps = mutableListOf<Number>()
	var remaining = string.substring(1)
	var current: Number? = null
	while (comps.size < 2) {
		when (remaining.first()) {
			'[' -> {
				val (n, str) = parseSnailFishNumber(remaining)
				current = n
				remaining = str
			}
			in digits -> {
				val (n, str) = parseRegularNumber(remaining)
				current = n
				remaining = str
			}
			',' -> {
				comps.add(current ?: throw IllegalStateException("no first number"))
				remaining = remaining.substring(1)
				current = null
			}
			']' -> {
				comps.add(current ?: throw IllegalStateException("no second number"))
			}
			else -> throw IllegalStateException("Unexpected char ${remaining.first()}")
		}
	}
	val pair = Pair(SnailfishNumber(comps.first(), comps.last()), remaining.substring(1))
	return pair
}

fun parseRegularNumber(chars: String): Pair<RegularNumber, String> {
	val end = chars.indexOfFirst { it !in digits }
	val n = chars.substring(0, end).toLong()
	val pair = Pair(RegularNumber(n), chars.substring(end))
	return pair
}

fun parse(string: String): SnailfishNumber {
	val (n, remaining) = parseSnailFishNumber(string)
	assert(remaining.isEmpty()) { "string not consumed" }
	return n
}

fun largestSum(numbers: List<SnailfishNumber>): Long? {
	return numbers.flatMap { first ->
		numbers.mapNotNull { second ->
			if (first == second) null
			else {
				val magnitude = (first + second).magnitude()
				magnitude
			}
		}
	}.maxOrNull()
}

fun main() {
	val numbers = generateSequence(::readLine)
		.map(::parse)
		.toList()

	println(largestSum(numbers))
}
