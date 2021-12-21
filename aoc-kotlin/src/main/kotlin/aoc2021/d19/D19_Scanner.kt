package aoc2021.d19

import java.time.Duration
import kotlin.math.abs
import kotlin.math.max
import kotlin.system.measureTimeMillis

typealias Translation = (Point) -> Point

val ROTATIONAL_TRANSFORMS = listOf<Translation>(
	{ p -> p },
	{ p -> p.rotateX() },
	{ p -> p.rotateX().rotateX() },
	{ p -> p.rotateX().rotateX().rotateX() },
)
	.flatMap { t ->
		listOf<Translation>(
			{ p -> t(p) },
			{ p -> t(p).rotateY() },
			{ p -> t(p).rotateY().rotateY() },
			{ p -> t(p).rotateY().rotateY().rotateY() },
		)
	}
	.flatMap { t ->
		listOf<Translation>(
			{ p -> t(p) },
			{ p -> t(p).rotateZ() },
			{ p -> t(p).rotateY().rotateZ() },
			{ p -> t(p).rotateZ().rotateZ().rotateZ() },
		)
	}

fun sq(x: Long) = x * x

data class Point(val x: Long, val y: Long, val z: Long) {
	fun distance2(other: Point): Long {
		return sq(other.x - this.x) + sq(other.y - this.y) + sq(other.z - this.z)
	}

	fun manhattanDistance(other: Point): Long {
		return abs(other.x - this.x) + abs(other.y - this.y) + abs(other.z - this.z)
	}

	operator fun plus(other: Point): Point {
		return Point(x + other.x, y + other.y, z + other.z)
	}

	operator fun minus(other: Point): Point {
		return Point(x - other.x, y - other.y, z - other.z)
	}

	fun shuffle(): Point {
		return Point(y, z, x)
	}

	fun rotateX(): Point {
		return Point(x, -z, y)
	}

	fun rotateY(): Point {
		return Point(z, y, -x)
	}

	fun rotateZ(): Point {
		return Point(y, -x, z)
	}
}

data class Scanner(val index: Int, val readings: List<Point>) {
	fun distance2s(point: Point): Set<Long> {
		return readings
			.map { point.distance2(it) }
			.filter { it > 0 }
			.toSet()
	}

	override fun toString(): String {
		return "Scanner $index"
	}
}

fun match(scanner1: Scanner, scanner2: Scanner): List<Triple<Point, Point, Int>> {
	val map2 = scanner2.readings.associateBy { it -> scanner2.distance2s(it) }
	return scanner1.readings
		.mapNotNull { p1 ->
			val dist2s = scanner1.distance2s(p1)
			val pointMatch = map2.entries
				.map { Pair(it.value, it.key.intersect(dist2s).size) }
				.filter { it.second > 1 }
				.maxByOrNull { it.second }
			pointMatch?.let { Triple(p1, it.first, it.second) }
		}
}

fun findTranslation(matches: List<Triple<Point, Point, Int>>): Translation {
	return ROTATIONAL_TRANSFORMS.firstNotNullOf { transform ->
		val delta = matches.first().let { m -> transform(m.second) - m.first }
		if (matches.all { m -> transform(m.second) - m.first == delta }) { p ->
			transform(p) - delta
		}
		else null
	}
}

fun generateTranslateGraph(scanners: List<Scanner>): Map<Pair<Scanner, Scanner>, Translation> {
	return scanners
		.flatMapIndexed { i, s1 ->
			scanners.subList(i + 1, scanners.size).flatMap { s2 ->
				var pairs = mutableListOf<Pair<Pair<Scanner, Scanner>, Translation>>()
				var m = match(s1, s2)
				if (m.size >= 12) {
					pairs.add(Pair(Pair(s2, s1), findTranslation(m)))
				}
				m = match(s2, s1)
				if (m.size >= 12) {
					pairs.add(Pair(Pair(s1, s2), findTranslation(m)))
				}
				pairs
			}
		}
		.toMap()
}

fun combineTranslations(
	start: Scanner,
	translationGraph: Map<Pair<Scanner, Scanner>, Translation>
): Map<Scanner, Translation> {
	val identity = { p: Point -> p }
	val map = mutableMapOf(Pair(start, identity))
	val queue = mutableListOf(Pair<Scanner?, Scanner>(null, start))
	while (queue.isNotEmpty()) {
		val (parent, scanner) = queue.removeFirst()
		if (parent != null) {
			val parentTranslation = map.getOrDefault(parent, identity)
			val translation = translationGraph.getOrDefault(Pair(scanner, parent), identity)
			map.putIfAbsent(scanner) { p -> parentTranslation(translation(p)) }
		}
		queue.addAll(
			translationGraph.keys
				.filter { (from, to) -> to == scanner && !map.containsKey(from) }
				.map { Pair(scanner, it.first) }
		)
	}
	return map
}

fun findPoints(scanners: List<Scanner>): Set<Point> {
	val translations = combineTranslations(
		scanners.first(),
		generateTranslateGraph(scanners)
	)
	return scanners.flatMap {
		val beacons = translations[it]?.let { translation ->
			it.readings.map(translation)
		} ?: emptyList()
		beacons
	}.toSet()
}

val SCANNER_RE = """^--- scanner \d+ ---$""".toRegex()
fun parse(lines: Iterable<String>): List<Scanner> {
	return lines
		.fold(mutableListOf<MutableList<Point>>()) { list, line ->
			if (SCANNER_RE.matches(line)) {
				list.add(mutableListOf())
			} else if (line.isNotEmpty()) {
				val (x, y, z) = line.split(",").map(String::toLong)
				list.last().add(Point(x, y, z))
			}
			list
		}
		.mapIndexed(::Scanner)
}

fun findMaxManhattanDistances(scanners: List<Scanner>): Long {
	val translations = combineTranslations(
		scanners.first(),
		generateTranslateGraph(scanners)
	)
	val origin = Point(0L, 0L, 0L)
	return scanners.foldIndexed(0L) { i, d, s1 ->
		val loc1 = translations[s1]?.let { it(origin) } ?: throw IllegalStateException()
		scanners.subList(i + 1, scanners.size).fold(d) { d2, s2 ->
			val loc2 = translations[s2]?.let { it(origin) } ?: throw IllegalStateException()
			val dist = loc1.manhattanDistance(loc2)
			max(d2, dist)
		}
	}
}

fun main() {
	val scanners = parse(generateSequence(::readLine).asIterable())
	val maxDist: Long
	println(Duration.ofMillis(measureTimeMillis {
		maxDist = findMaxManhattanDistances(scanners)
	}))
	println(maxDist)
}
