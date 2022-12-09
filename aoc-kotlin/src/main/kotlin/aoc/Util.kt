package aoc

import java.io.File

fun readInput(name: String) = File("src/main/resources/input", name).readLines()

fun readTestInput(name: String) = File("src/test/resources/input", name).readLines()

