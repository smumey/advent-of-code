package aoc2022.d07

import readInput

class Directory(val parent: Directory?) {
	val files = mutableMapOf<String, Long>()
	val directories = mutableMapOf<String, Directory>()
	override fun toString(): String {
		return "Directory(files=$files, directories=$directories)"
	}

	fun sumSmall(thresholdSize: Long): Pair<Long, Long> {
		val childSizes = directories.values.map { it.sumSmall(thresholdSize) }.fold(Pair(0L, 0L)) { p1, p2 ->
			Pair(p1.first + p2.first, p1.second + p2.second)
		}
		val size = childSizes.second + files.values.sum()
		return Pair(
			childSizes.first + if (size <= thresholdSize) {
				size
			} else {
				0L
			},
			size
		)
	}
}

enum class Command {
	CD,
	LS
}

enum class Mode {
	COMMAND,
	LIST
}

const val COMMAND_INDICATOR = "$"
const val DIR_INDICATOR = "dir"
const val LS = "ls"
const val CD = "cd"
const val ROOT_DIR = "/"
const val PARENT_DIR = ".."

fun parse(input: List<String>): Directory {
	val root = Directory(null)
	var mode = Mode.COMMAND
	var workDir: Directory = root
	for (line in input) {
		val tokens = line.split(" ")
		when {
			tokens[0] == COMMAND_INDICATOR -> {
				mode = Mode.COMMAND
				when (tokens[1]) {
					CD -> {
						workDir = when (tokens[2]) {
							ROOT_DIR -> {
								root
							}

							PARENT_DIR -> {
								workDir.parent ?: root
							}

							else -> {
								workDir.directories[tokens[2]] ?: workDir
							}
						}
					}

					LS -> {
						mode = Mode.LIST
					}
				}
			}

			tokens[0] == DIR_INDICATOR -> {
				assert(mode == Mode.LIST)
				workDir.directories.putIfAbsent(tokens[1], Directory(workDir))
			}

			else -> {
				assert(mode == Mode.LIST)
				workDir.files.putIfAbsent(tokens[1], tokens[0].toLong())
			}
		}
	}
	return root
}

fun main() {
	println(parse(readInput("aoc2022/7")).sumSmall(100_000L).first)
}
