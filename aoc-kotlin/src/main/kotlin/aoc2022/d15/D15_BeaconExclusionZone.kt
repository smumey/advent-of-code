package aoc2022.d15

import aoc.LongCoordinate
import aoc.readInput
import kotlin.math.abs

data class Sensor(val location: LongCoordinate, val closestBeacon: LongCoordinate) {
	fun distance(coordinate: LongCoordinate) = abs(coordinate.x - location.x) + abs(coordinate.y - location.y)

	fun findCoveredPositions(y: Long): List<LongCoordinate> {
		val beaconDistance = distance(closestBeacon)
		val yDist = abs(y - location.y)
		if (yDist > beaconDistance) return listOf()
		return ((location.x - (beaconDistance - yDist))..(location.x + (beaconDistance - yDist))).map {
			LongCoordinate(it, y)
		}
	}
}

fun parse(line: String): Sensor {
	val locX: Long
	val locY: Long
	val beaconX: Long
	val beaconY: Long
	line.substring("Sensor at x=".length).let { locXSub ->
		val locXEnd = locXSub.indexOf(',')
		locX = locXSub.substring(0, locXEnd).toLong()
		locXSub.substring(locXEnd + ", y=".length).let { locYSub ->
			val locYEnd = locYSub.indexOf(':')
			locY = locYSub.substring(0, locYEnd).toLong()
			locYSub.substring(locYEnd + ": closest beacon is at x=".length).let { beaconXSub ->
				val beaconXEnd = beaconXSub.indexOf(',')
				beaconX = beaconXSub.substring(0, beaconXEnd).toLong()
				beaconXSub.substring(beaconXEnd + ", y=".length).let { beaconYSub ->
					beaconY = beaconYSub.toLong()
				}
			}
		}
	}
	return Sensor(LongCoordinate(locX, locY), LongCoordinate(beaconX, beaconY))

}

fun findBeaconExclusionCount(sensors: List<Sensor>, y: Long): Int {
	return sensors.flatMap { it.findCoveredPositions(y) }.toSet().minus(sensors.map { it.closestBeacon }.toSet()).size
}

fun main() {
	val sensors = readInput("aoc2022/15").map(::parse)
	println(findBeaconExclusionCount(sensors, 2000000))
}
