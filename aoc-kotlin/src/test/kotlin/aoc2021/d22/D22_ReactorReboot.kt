package aoc2021.d22

import java.time.Duration
import java.util.BitSet
import kotlin.system.measureTimeMillis

data class Point(val x: Int, val y: Int, val z: Int) {
	operator fun minus(other: Point): Point {
		return Point(x - other.x, y - other.y, z - other.z)
	}
}

data class RebootStep(val toggle: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

fun initCubesRegion(sideRange: IntRange): CubesRegion {
	val dim = sideRange.count()
	return CubesRegion(BitSet(dim * dim * dim), sideRange)
}

data class CubesRegion(val bits: BitSet, val sideRange: IntRange) {
	private val dim = sideRange.count()
	val min = sideRange.first
	private val minPoint = Point(sideRange.first, sideRange.first, sideRange.first)

	fun fromPoint(p: Point): Int {
		val nP = p - minPoint
		return nP.z * dim * dim + nP.y * dim + nP.x
	}

	fun fromPoint(x: Int, y: Int, z: Int): Int {
		return fromPoint(Point(x, y, z))
	}

	fun reboot(step: RebootStep): CubesRegion {
		val newBits = BitSet(bits.size())
		newBits.or(bits)
		return CubesRegion(
			step.zRange.intersect(sideRange).fold(newBits) { bitsZ, z ->
				step.yRange.intersect(sideRange).fold(bitsZ) { bitsY, y ->
					step.xRange.intersect(sideRange).fold(bitsY) { bitsX, x ->
						bitsX.set(fromPoint(x, y, z), step.toggle)
						bitsX
					}
				}
			},
			sideRange
		)
	}

	fun reboot(steps: List<RebootStep>): CubesRegion {
		return steps.fold(this) { c, r -> c.reboot(r) }
	}

	fun count(): Int {
		return bits.cardinality()
	}
}

const val rangeRe = """(-?\d+)\.\.(-?\d+)"""
val rebootRe = "^(on|off) x=$rangeRe,y=$rangeRe,z=$rangeRe\$".toRegex()

fun parse(lines: Iterable<String>): List<RebootStep> {
	return lines.mapNotNull {
		rebootRe.find(it)?.let { m ->
			RebootStep(
				m.groupValues[1] == "on",
				m.groupValues[2].toInt()..m.groupValues[3].toInt(),
				m.groupValues[4].toInt()..m.groupValues[5].toInt(),
				m.groupValues[6].toInt()..m.groupValues[7].toInt()
			)
		}
	}.toList()
}

fun main() {
	val steps = parse(generateSequence(::readLine).asIterable())
	val region = initCubesRegion(-50..50)
	val count: Int
	println(Duration.ofMillis(measureTimeMillis { count = region.reboot(steps).count() }))
	println(count)
}
