package aoc2021.d20

import java.time.Duration
import java.util.BitSet
import kotlin.system.measureTimeMillis

data class Image(val bits: BitSet, val nRows: Int, val nCols: Int, val infinity: Boolean) {
	fun getValue(x: Int, y: Int): Int {
		val value = ((y - 1)..(y + 1)).fold(0) { n, pY ->
			((x - 1)..(x + 1)).fold(n) { n2, pX ->
				(n2 shl 1) + if (getBit(pX, pY)) 1 else 0
			}
		}
		return value
	}

	private fun getBit(x: Int, y: Int): Boolean {
		return if (x in 0 until nCols && y in 0 until nRows) bits[nCols * y + x]
		else infinity
	}

	fun step(algorithm: BitSet): Image {
		val expanded = BitSet((nCols + 4) * (nRows + 4))
		(0..nRows + 4).forEach { newY ->
			val y = newY - 2
			(0..nCols + 4).forEach { newX ->
				val x = newX - 2
				expanded[(nCols + 4) * newY + newX] = algorithm[getValue(x, y)]
			}
		}
		return Image(expanded, nRows + 4, nCols + 4, algorithm[if (infinity) 0x1ff else 0])
	}

	fun step(algorithm: BitSet, times: Int): Image {
		return generateSequence(this) { it.step(algorithm) }.elementAt(times)
	}

	fun litCount(): Int {
		return bits.cardinality()
	}

	override fun toString(): String {
		return (0 until nRows).fold(StringBuilder()) { s, y ->
			(0 until nCols).fold(s) { s2, x ->
				s2.append(if (bits[nCols * y + x]) '#' else '.')
			}
			s.append('\n')
		}.toString()
	}
}

fun parseAlgorithm(line: String): BitSet {
	val algorithm = BitSet(line.length)
	for (i in line.indices) {
		algorithm[i] = line[i] == '#'
	}
	return algorithm
}

fun parseImage(lines: List<String>): Image {
	val nCols = lines.first().length
	val image = BitSet(lines.size * nCols)

	for (y in lines.indices) {
		val line = lines[y]
		for (x in line.indices) {
			image[nCols * y + x] = line[x] == '#'
		}
	}
	return Image(image, lines.size, nCols, false)
}

fun main() {
	val lines = generateSequence { readLine() }.toList()
	val algorithm = parseAlgorithm(lines[0])
	val image = parseImage(lines.subList(2, lines.size))
	val count: Int
	println(Duration.ofMillis(measureTimeMillis {
		count = image.step(algorithm, 50).litCount()
	}))
	println(count)
}
