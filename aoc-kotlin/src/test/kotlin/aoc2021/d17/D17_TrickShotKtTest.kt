package aoc2021.d17

import kotlin.test.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class D17_TrickShotKtTest {
	val expectedTraj = setOf(
		Pair(23, -10),
		Pair(25, -9),
		Pair(27, -5),
		Pair(29, -6),
		Pair(22, -6),
		Pair(21, -7),
		Pair(9, 0),
		Pair(27, -7),
		Pair(24, -5),
		Pair(25, -7),
		Pair(26, -6),
		Pair(25, -5),
		Pair(6, 8),
		Pair(11, -2),
		Pair(20, -5),
		Pair(29, -10),
		Pair(6, 3),
		Pair(28, -7),
		Pair(8, 0),
		Pair(30, -6),
		Pair(29, -8),
		Pair(20, -10),
		Pair(6, 7),
		Pair(6, 4),
		Pair(6, 1),
		Pair(14, -4),
		Pair(21, -6),
		Pair(26, -10),
		Pair(7, -1),
		Pair(7, 7),
		Pair(8, -1),
		Pair(21, -9),
		Pair(6, 2),
		Pair(20, -7),
		Pair(30, -10),
		Pair(14, -3),
		Pair(20, -8),
		Pair(13, -2),
		Pair(7, 3),
		Pair(28, -8),
		Pair(29, -9),
		Pair(15, -3),
		Pair(22, -5),
		Pair(26, -8),
		Pair(25, -8),
		Pair(25, -6),
		Pair(15, -4),
		Pair(9, -2),
		Pair(15, -2),
		Pair(12, -2),
		Pair(28, -9),
		Pair(12, -3),
		Pair(24, -6),
		Pair(23, -7),
		Pair(25, -10),
		Pair(7, 8),
		Pair(11, -3),
		Pair(26, -7),
		Pair(7, 1),
		Pair(23, -9),
		Pair(6, 0),
		Pair(22, -10),
		Pair(27, -6),
		Pair(8, 1),
		Pair(22, -8),
		Pair(13, -4),
		Pair(7, 6),
		Pair(28, -6),
		Pair(11, -4),
		Pair(12, -4),
		Pair(26, -9),
		Pair(7, 4),
		Pair(24, -10),
		Pair(23, -8),
		Pair(30, -8),
		Pair(7, 0),
		Pair(9, -1),
		Pair(10, -1),
		Pair(26, -5),
		Pair(22, -9),
		Pair(6, 5),
		Pair(7, 5),
		Pair(23, -6),
		Pair(28, -10),
		Pair(10, -2),
		Pair(11, -1),
		Pair(20, -9),
		Pair(14, -2),
		Pair(29, -7),
		Pair(13, -3),
		Pair(23, -5),
		Pair(24, -8),
		Pair(27, -9),
		Pair(30, -7),
		Pair(28, -5),
		Pair(21, -10),
		Pair(7, 9),
		Pair(6, 6),
		Pair(21, -5),
		Pair(27, -10),
		Pair(7, 2),
		Pair(30, -9),
		Pair(21, -8),
		Pair(22, -7),
		Pair(24, -9),
		Pair(20, -6),
		Pair(6, 9),
		Pair(29, -5),
		Pair(8, -2),
		Pair(27, -8),
		Pair(30, -5),
		Pair(24, -7)
	)

	@Test
	fun testParseTarget() {
		val input = "target area: x=20..30, y=-10..-5"
		assertEquals(Pair(20..30, -10..-5), parseTarget(input))
	}

	@Test
	fun testYPosition() {
		assertEquals(45, yPosition(9, 9))
	}

	@Test
	fun testFindMaximum() {
		assertEquals(9, findMaximum(Triple(0, 9, 1)))
	}

	@Test
	fun testFindVels() {
		val trajectories = findTrajectories(Point(21, -9))
		trajectories.forEach { traj ->
			println((0..traj.third).map {
				Triple(
					xPosition(traj.first, it),
					yPosition(traj.second, it),
					it
				)
			}.toList())
		}
	}

	@Test
	fun testThatGivenInputWorks() {
		val traj = Pair(9, 1)
		val target = parseTarget("target area: x=20..30, y=-10..-5")
		var t = 0
		println(target)
		while (yPosition(traj.second, t) >= target.second.first) {
			val point = Point(xPosition(traj.first, t), yPosition(traj.second, t))
			println("point $point in ${target.first.contains(point.x) && target.second.contains(point.y)}")
			t += 1
		}
	}

	@Test
	fun testFindMaximumOverTarget() {
		val target = parseTarget("target area: x=20..30, y=-10..-5")
		assertEquals(45, findMaximumOverTarget(target))
	}

	@Test
	@Disabled
	fun testVelCountTarget() {
		val target = parseTarget("target area: x=20..30, y=-10..-5")
		val trajectories = findDistinctTrajectoriesOverTarget(target)
		println(trajectories - expectedTraj)
		assertEquals(expectedTraj, trajectories)
		assertEquals(112, trajectories.size)
	}
}
