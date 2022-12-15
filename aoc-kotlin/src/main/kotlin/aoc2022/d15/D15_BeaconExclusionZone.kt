package aoc2022.d15

import aoc.Coordinate
import aoc.readInput
import java.util.BitSet
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Sensor(val location: Coordinate, val closestBeacon: Coordinate) {
	fun distance(coordinate: Coordinate) = abs(coordinate.x - location.x) + abs(coordinate.y - location.y)

	fun findCoveredPositions(y: Int): IntRange {
		val beaconDistance = distance(closestBeacon)
		val yDist = abs(y - location.y)
		if (yDist > beaconDistance) return IntRange.EMPTY
		return ((location.x - (beaconDistance - yDist))..(location.x + (beaconDistance - yDist)))
	}
}

fun parse(line: String): Sensor {
	val locX: Int
	val locY: Int
	val beaconX: Int
	val beaconY: Int
	line.substring("Sensor at x=".length).let { locXSub ->
		val locXEnd = locXSub.indexOf(',')
		locX = locXSub.substring(0, locXEnd).toInt()
		locXSub.substring(locXEnd + ", y=".length).let { locYSub ->
			val locYEnd = locYSub.indexOf(':')
			locY = locYSub.substring(0, locYEnd).toInt()
			locYSub.substring(locYEnd + ": closest beacon is at x=".length).let { beaconXSub ->
				val beaconXEnd = beaconXSub.indexOf(',')
				beaconX = beaconXSub.substring(0, beaconXEnd).toInt()
				beaconXSub.substring(beaconXEnd + ", y=".length).let { beaconYSub ->
					beaconY = beaconYSub.toInt()
				}
			}
		}
	}
	return Sensor(Coordinate(locX, locY), Coordinate(beaconX, beaconY))

}

data class BitSetWithOffset(val bitSet: BitSet, val minX: Int)

fun findBeaconExclusions(sensors: List<Sensor>, y: Int): BitSetWithOffset {
	val bitSet = BitSet()
	val ranges = sensors.map { it.findCoveredPositions(y) }
	val minX = min(ranges.minOf { it.first }, sensors.minOf { it.closestBeacon.x })
	ranges.forEach {
		bitSet.set(it.first - minX, it.last + 1 - minX)
	}
	sensors.filter { it.closestBeacon.y == y }.map { it.closestBeacon.x }.forEach {
		bitSet.clear(it - minX)
	}
	return BitSetWithOffset(bitSet, minX)
}

fun tuningFrequency(coordinate: Coordinate): Long {
	return coordinate.x * 4000000L + coordinate.y
}

fun findBeaconLocation(sensors: List<Sensor>, maxX: Int, maxY: Int): Coordinate? {
	val bitSet = BitSet()
	val pair = (0..maxY).asSequence()
		.map { y ->
			bitSet.clear()
			val ranges = sensors.map { it.findCoveredPositions(y) }
			ranges.forEach {
				bitSet.set(max(it.first, 0), min(it.last, maxX) + 1)
			}
			Pair(y, bitSet)

		}
		.find { it.second.nextClearBit(0) <= maxX }
	return pair?.let {
		val x = it.second.nextClearBit(0)
		Coordinate(x, it.first)
	}
}

fun main() {
	val sensors = readInput("aoc2022/15").map(::parse)
	println(findBeaconExclusions(sensors, 2000000).bitSet.cardinality())
}
