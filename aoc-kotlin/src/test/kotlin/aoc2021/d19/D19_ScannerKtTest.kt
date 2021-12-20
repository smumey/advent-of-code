package aoc2021.d19

import java.io.InputStreamReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class D19_ScannerKtTest {
	private var lines: List<String> = listOf()

	@BeforeEach
	fun setUp() {
		lines = InputStreamReader(
			D19_ScannerKtTest::class.java.getResourceAsStream("sample.txt")
		).use(InputStreamReader::readLines)
	}

	@Test
	fun testScanner() {
		val scanners = parse(lines)
		assertEquals(5, scanners.size)
	}

	@Test
	fun testMatch() {
		val scanners = parse(lines)
		println(match(scanners[1], scanners[4]))
	}

	@Test
	fun testFindTransform() {
		val scanners = parse(lines)
		val translation = findTranslation(match(scanners[0], scanners[1]))
		val points1 = scanners[1].readings.map { p -> translation(p) }.toSet()
		assertTrue(points1.intersect(scanners[0].readings.toSet()).size >= 12)
	}

	@Test
	fun testRotations() {
		val point = Point(1, 2, 3)
		val rots = ROTATIONAL_TRANSFORMS.map { it -> it(point) }
		assertEquals(24, rots.toSet().size)
	}

	@Test
	fun testFindPoints() {
		val scanners = parse(lines)
		val beacons = findPoints(scanners)
		assertEquals(79, beacons.size)
	}

	@Test
	fun testMaxManhattanDistance() {
		val scanners = parse(lines)
		assertEquals(3621L, findMaxManhattanDistances(scanners))
	}
}
