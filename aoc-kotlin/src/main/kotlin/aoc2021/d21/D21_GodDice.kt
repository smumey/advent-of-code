package aoc2021.d21

import java.time.Duration
import kotlin.system.measureTimeMillis

val FREQS = (1..3).flatMap { i ->
	(1..3).flatMap { j ->
		(1..3).map { k ->
			(i + j + k)
		}
	}
}
	.groupBy { it }
	.mapValues { it.value.size.toLong() }

val ZEROES = (0..10).map { 0L }.toList()

fun nextPosition(pos: Int, rollSum: Int): Int {
	return (pos - 1 + rollSum) % 10 + 1
}

data class PlayerState(val score: Int, val position: Int) {
	fun hasWon(): Boolean {
		return score >= 21
	}

	fun nextPositionCounts(): List<Long> {
		val nextPositions = ZEROES.toMutableList()
		FREQS.forEach { (rollSum, rollCount) ->
			nextPositions[nextPosition(position, rollSum)] += rollCount
		}
		return nextPositions
	}
}

data class GameState(val players: List<PlayerState>) {
	val playerCount = players.size
	fun winner(): Int? {
		return players.indexOfFirst { it.hasWon() }.let { if (it >= 0) it else null }
	}

	private fun activePlayer(turn: Int) = (turn + 1) % playerCount

	fun takeTurn(turn: Int): Map<GameState, Long> {
		val player = activePlayer(turn)
		val playerState = players[player]
		return playerState.nextPositionCounts().mapIndexedNotNull { pos, count ->
			if (count > 0) {
				val nextPlayerStates = this.players.toMutableList()
				nextPlayerStates[player] = PlayerState(
					playerState.score + pos,
					pos
				)
				Pair(GameState(nextPlayerStates), count)
			} else null
		}.toMap()
	}
}

fun calculateGameStates(initialPositions: List<Int>): Sequence<Pair<Int, Map<GameState, Long>>> {
	val initialGameStateState = GameState(initialPositions.map { PlayerState(0, it) })
	return generateSequence(Pair(1, mapOf(Pair(initialGameStateState, 1L)))) { (turn, map) ->
		if (map.keys.all { it.winner() != null }) null
		else {
			val nextMap = mutableMapOf<GameState, Long>()
			map.entries.forEach { (state, count) ->
				if (state.winner() == null) {
					state.takeTurn(turn).forEach { (newState, factor) ->
						nextMap.merge(newState, count * factor, Long::plus)
					}
				} else {
					nextMap.merge(state, count, Long::plus)
				}
			}
			Pair(turn + 1, nextMap)
		}
	}
}

fun findMostWins(state: Map<GameState, Long>): Long {
	return state.entries.groupBy { it.key.winner() ?: -1 }
		.mapValues { it.value.sumOf { v -> v.value } }
		.maxOf { it.value }
}

fun main() {
	val max: Long
	println(Duration.ofMillis(measureTimeMillis {
		max = findMostWins(calculateGameStates(listOf(9, 3)).last().second)
	}))
	println(max)
}
