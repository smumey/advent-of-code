package aoc2022.d04

import org.junit.jupiter.api.Test
import readTestInput
import kotlin.test.assertEquals

class D04_SectionAssignmentsKtTest {
	private val assignmentPairs = readTestInput("aoc2022/4").map { parse(it) }

	@Test
	fun countContainedAssignments() {
		assertEquals(2, countContainedAssignments(assignmentPairs))
	}

	@Test
	fun countOverlappingAssignments() {
		assertEquals(4, countOverlappingAssignments(assignmentPairs))
	}
}