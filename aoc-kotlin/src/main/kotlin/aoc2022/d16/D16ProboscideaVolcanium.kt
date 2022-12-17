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
			rateS.substring(rateEnd + "; tunnels lead to valves ".length).let { tunnelS ->
				tunnels = tunnelS.split(", ").toSet()
			}
		}
	}
	return Valve(label, rate, tunnels)
}

