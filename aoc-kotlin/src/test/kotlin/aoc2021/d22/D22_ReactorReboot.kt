package aoc2021.d22

import java.lang.Integer.max
import java.lang.Integer.min
import java.time.Duration
import kotlin.system.measureTimeMillis

data class Region(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {
	fun intersection(other: Region): Region? {
		val intersection = Region(
			max(xRange.first, other.xRange.first)..min(xRange.last, other.xRange.last),
			max(yRange.first, other.yRange.first)..min(yRange.last, other.yRange.last),
			max(zRange.first, other.zRange.first)..min(zRange.last, other.zRange.last),
		)
		return if (
			intersection.xRange.first <= intersection.xRange.last &&
			intersection.yRange.first <= intersection.yRange.last &&
			intersection.zRange.first <= intersection.zRange.last
		) intersection
		else null
	}

	fun contains(other: Region): Boolean {
		return xRange.first <= other.xRange.first && other.xRange.last <= xRange.last &&
				yRange.first <= other.yRange.first && other.yRange.last <= yRange.last &&
				zRange.first <= other.zRange.first && other.zRange.last <= zRange.last
	}

	fun volume(): Long {
		return (xRange.last - xRange.first + 1).toLong() *
				(yRange.last - yRange.first + 1).toLong() *
				(zRange.last - zRange.first + 1).toLong()
	}
}

data class RebootStep(val setOn: Boolean, val region: Region) {
	fun process(other: RebootStep): RebootStep? {
		val intersection = region.intersection(other.region)
		return if (intersection == null) null else RebootStep(!this.setOn, intersection)
	}

	fun value(): Long {
		return (if (setOn) 1L else -1L) * region.volume()
	}
}

fun initReactor(): Reactor {
	return Reactor(listOf())
}

data class Reactor(val steps: List<RebootStep>) {
	fun reboot(step: RebootStep): Reactor {
		val nextSteps = steps.toMutableList()
		if (step.setOn) {
			nextSteps.add(step)
		}
		nextSteps.addAll(steps.mapNotNull { it.process(step) }.toList())
		return Reactor(nextSteps)
	}

	fun reboot(steps: List<RebootStep>): Reactor {
		val result = steps.fold(this) { c, r -> c.reboot(r) }
		return result
	}

	fun count(): Long {
		return steps.map(RebootStep::value).sum()
	}
}

const val rangeRe = """(-?\d+)\.\.(-?\d+)"""
val rebootRe = "^(on|off) x=$rangeRe,y=$rangeRe,z=$rangeRe\$".toRegex()

fun parse(lines: Iterable<String>): List<RebootStep> {
	return lines.mapNotNull {
		rebootRe.find(it)?.let { m ->
			RebootStep(
				m.groupValues[1] == "on",
				Region(
					m.groupValues[2].toInt()..m.groupValues[3].toInt(),
					m.groupValues[4].toInt()..m.groupValues[5].toInt(),
					m.groupValues[6].toInt()..m.groupValues[7].toInt()
				)
			)
		}
	}.toList()
}

fun main() {
	val steps = parse(generateSequence(::readLine).asIterable())
	val region = initReactor()
	val count: Long
	println(Duration.ofMillis(measureTimeMillis { count = region.reboot(steps).count() }))
	println(count)
}
