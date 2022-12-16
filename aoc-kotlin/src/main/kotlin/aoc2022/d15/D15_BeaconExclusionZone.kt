package aoc2022.d15

import aoc.Coordinate
import aoc.distinctRanges
import aoc.readInput
import kotlin.math.abs

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

fun findBeaconExclusionsCount(sensors: List<Sensor>, y: Int): Int {
    val ranges = distinctRanges(sensors.map { it.findCoveredPositions(y) })
    val beacons = sensors.map { s -> s.closestBeacon }.toSet()

    return ranges.map {
        it.last - it.first + 1 - beacons.count { b -> b.y == y && it.contains(b.x) }
    }.sum()
}

fun tuningFrequency(coordinate: Coordinate): Long {
    return coordinate.x * 4000000L + coordinate.y
}

fun findBeaconLocation(sensors: List<Sensor>, maxX: Int, maxY: Int): Coordinate? {
    return (0..maxY).asSequence()
            .map { y ->
                val ranges = distinctRanges(sensors.map { it.findCoveredPositions(y) })
                if (ranges.size > 1) {
                    val x = when {
                        ranges[0].first > 0 -> 0
                        ranges[1].last < maxX -> maxX
                        else -> ranges[0].last + 1
                    }
                    Coordinate(x, y)
                } else {
                    null
                }
            }
            .find { it != null }
}

fun main() {
    val sensors = readInput("aoc2022/15").map(::parse)
    println(findBeaconExclusionsCount(sensors, 2000000))
}
