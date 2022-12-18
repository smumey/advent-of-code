package aoc2022.d16

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

private const val TIME_LIMIT = 30

fun findMaxPressure(
	location: Valve,
	unvisited: List<Valve>,
	time: Int,
	pressure: Int,
	distance: (Valve, Valve) -> Int
): Int {
	val possible = unvisited.filter {
		distance(it, location) + time < TIME_LIMIT
	}
	return possible.maxOfOrNull {
		val releaseTime = time + distance(it, location) + 1
		val addedPressure = (TIME_LIMIT - releaseTime) * it.rate
		findMaxPressure(it, possible.minus(it), releaseTime, pressure + addedPressure, distance)
	} ?: pressure
}

data class Agent(val valve: Valve, val time: Int)

fun findMaxPressureWithElephant(
	agents: List<Agent>,
	unvisited: List<Valve>,
	pressure: Int,
	distance: (Valve, Valve) -> Int,
	timeLimit: Int
): Int {
	return unvisited.maxOfOrNull { valve ->
		agents.maxOf { agent ->
			val releaseTime = agent.time + distance(agent.valve, valve) + 1
			if (releaseTime >= timeLimit) {
				pressure
			} else {
				val addedPressure = (timeLimit - releaseTime) * valve.rate
				findMaxPressureWithElephant(
					agents.map { if (it === agent) Agent(valve, releaseTime) else it },
					unvisited.minus(valve),
					pressure + addedPressure,
					distance,
					timeLimit
				)
			}
		}
	} ?: pressure
}

fun findMaxPressure(valves: List<Valve>, startLabel: String): Int? {
	return valves.find { it.label == startLabel }?.let { start ->
		findMaxPressure(start, valves.filter { it.rate > 0 }, 0, 0, findDistances(valves))
	}
}

fun findMaxPressureWithElephant(valves: List<Valve>, startLabel: String, timeLimit: Int): Int? {
	return valves.find { it.label == startLabel }?.let { start ->
		findMaxPressureWithElephant(
			listOf(Agent(start, 0), Agent(start, 0)),
			valves.filter { it.rate > 0 },
			0,
			findDistances(valves),
			timeLimit
		)
	}
}
