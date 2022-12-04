package aoc2022.d04

import readInput

fun contains(container: IntRange, containee: IntRange): Boolean {
	return container.first <= containee.first && containee.last <= container.last
}

data class AssignmentPair(val one: IntRange, val two: IntRange) {
	fun hasContainedAssignment() = contains(one, two) || contains(two, one)

	fun hasOverlappingAssignment() = one.first <= two.last && two.first <= one.last
}

fun parse(line: String): AssignmentPair {
	val (one, two) = line.split(",")
		.map { val (low, high) = it.split("-").map(String::toInt); low..high }
	return AssignmentPair(one, two)
}

fun countContainedAssignments(assignments: Iterable<AssignmentPair>) =
	assignments.count(AssignmentPair::hasContainedAssignment)

fun countOverlappingAssignments(assignments: Iterable<AssignmentPair>) =
	assignments.count(AssignmentPair::hasOverlappingAssignment)

fun main() {
	println(countOverlappingAssignments(readInput("aoc2022/4").map(::parse)))
}