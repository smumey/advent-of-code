package aoc2025

import aoc.Coordinate
import me.sol.Answer
import me.sol.Utility
import java.util.*
import java.util.stream.Stream

@JvmRecord
data class D09MovieTheatre(val coordinates: List<Coordinate?>?) {
    @Answer
    fun p1LargestArea(): Int {

    }
}

fun parse(input: Stream<String?>): D09MovieTheatre {
    return D09MovieTheatre(input.map<Coordinate?> { s: String? ->
        val scanner = Scanner(s)
        scanner.useDelimiter(",")
        Coordinate(scanner.nextInt(), scanner.nextInt())
    }.toList())
}


fun main() {
    Utility.execute(Utility.readInput(D09MovieTheatre::class.java, ::parse))
}
