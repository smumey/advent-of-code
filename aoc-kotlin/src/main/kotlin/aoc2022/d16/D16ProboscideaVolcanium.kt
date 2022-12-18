package aoc2022.d16

import kotlin.math.max

data class Valve(val label: String, val rate: Int, val tunnels: Set<String>)

fun parse(line: String): Valve {
	val label: String
	val rate: Int
	val tunnels: Set<String>
	line.substring("Valve ".length).let { labelS ->
		val labelEnd = labelS.indexOf(' ')
		label = labelS.substring(0, labelEnd)
		labelS.substring(labelEnd + " has flow rate=".length).let { rateS ->
			val rateEnd = rateS.indexOf(';')
			rate = rateS.substring(0, rateEnd).toInt()
			rateS.substring(rateEnd + "; ".length).let { tunnelS ->
				if (tunnelS.startsWith("tunnels")) {
					tunnelS.substring("tunnels lead to valves ".length).let { listS ->
						tunnels = listS.split(", ").toSet()
					}
				} else {
					tunnelS.substring("tunnel leads to valve ".length).let { listS ->
						tunnels = setOf(listS)
					}
				}
			}
		}
	}
	return Valve(label, rate, tunnels)
}

fun infinityAdd(x: Int, y: Int) = when (x) {
	Int.MAX_VALUE -> x
	else -> x + y
}

fun getConnect(valves: List<Valve>): (Valve) -> List<Valve> {
	val map = valves.associateWith {
		valves.filter { v -> v.label in it.tunnels }
	}
	return { v -> map.getOrDefault(v, listOf()) }
}

fun findDistance(valves: List<Valve>, connect: (Valve) -> List<Valve>, source: Valve): Map<Valve, Int> {
	val dist = mutableMapOf<Valve, Int>()
	dist[source] = 0
	fun distance(valve: Valve) = dist.getOrDefault(valve, Int.MAX_VALUE)
	val prev = mutableMapOf<Valve, Valve>()
	val queue = valves.toMutableList()
	queue.add(source)
	while (queue.isNotEmpty()) {
		queue.sortByDescending { distance(it) }
		val u = queue.removeLast()
		connect(u).filter { queue.contains(it) }.forEach { v ->
			val alt = infinityAdd(distance(u), 1)
			if (alt < distance(v)) {
				dist[v] = alt
				prev[v] = u
			}
		}
	}
	return dist
}

fun findDistances(valves: List<Valve>): (Valve, Valve) -> Int {
	val connect = getConnect(valves)
	val activeValves = valves.filter { it.rate > 0 }
	val distances = (activeValves + valves).associateWith {
		findDistance(valves, connect, it)
	}
	return { d1, d2 ->
		distances[d1]?.get(d2) ?: Int.MAX_VALUE
	}
}

fun visit(
	valve: Valve,
	unstuck: List<Valve>,
	openValves: Set<Valve>,
	timeRemaining: Int,
	pressure: Int,
	distance: (Valve, Valve) -> Int,
	statePressure: MutableMap<Set<Valve>, Int>
): Map<Set<Valve>, Int> {
	statePressure[openValves] = max(statePressure.getOrDefault(openValves, 0), pressure)
	unstuck.forEach { v ->
		val newTimeRemaining = timeRemaining - distance(valve, v) - 1
		if (!(openValves.contains(v) || newTimeRemaining <= 0)) {
			visit(
				v,
				unstuck,
				openValves + v,
				newTimeRemaining,
				pressure + newTimeRemaining * v.rate,
				distance,
				statePressure
			)
		}
	}
	return statePressure
}

fun findMaxPressure(valves: List<Valve>, startLabel: String): Int? {
	return valves.find { it.label == startLabel }?.let { start ->
		val statePressure =
			visit(start, valves.filter { it.rate > 0 }, setOf(), 30, 0, findDistances(valves), mutableMapOf())
		return statePressure.values.maxOrNull()
	}
}

fun findMaxPressureWithElephant(valves: List<Valve>, startLabel: String, timeLimit: Int): Int? {
	return valves.find { it.label == startLabel }?.let { start ->
		val statePressure =
			visit(start, valves.filter { it.rate > 0 }, setOf(), timeLimit, 0, findDistances(valves), mutableMapOf())
		return statePressure.entries.fold(0) { p1, e1 ->
			statePressure.entries.fold(p1) { p2, e2 ->
				if (e1.key.intersect(e2.key).isNotEmpty()) p2
				else max(p2, e1.value + e2.value)
			}
		}
	}
}
