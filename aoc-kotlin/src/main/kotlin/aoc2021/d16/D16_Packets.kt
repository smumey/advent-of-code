package aoc2021.d16

import java.util.function.Function

const val HEADER_LENGTH = 6

fun toIntArray(hex: String): IntArray {
	if (hex.length % 2 != 0) {
		throw IllegalArgumentException("not a sequence of bytes!")
	}
	return (hex.indices step 2).map {
		hex.substring(it, it + 2).toInt(16)
	}.toIntArray()
}

fun toBinaryString(input: IntArray): String {
	return input.fold(StringBuilder()) { builder, byte ->
		var value = byte
		for (i in 0 until Byte.SIZE_BITS) {
			builder.append(if (value and 128 == 0) 0 else 1)
			value = value shl 1
		}
		builder
	}.toString()
}

fun toLong(input: String): Long {
	return input.fold(0L) { n, bit ->
		n * 2 + if (bit == '1') 1L else 0L
	}
}

fun readPacketFromHex(hex: String): Packet {
	return readPacket(toBinaryString(toIntArray(hex)))
}

fun readPacket(bits: String): Packet {
	val version = toLong(bits.substring(0, 3))
	val type = toLong(bits.substring(3, 6))
	return when (type) {
		4L -> LiteralPacket(version, type, bits.substring(HEADER_LENGTH))
		else -> OperatorPacket(version, type, bits.substring(HEADER_LENGTH))
	}
}

fun sumVersions(packet: Packet): Long {
	return packet.version + packet.subpackets.sumOf(::sumVersions)
}

fun main() {
	println(readPacketFromHex(readLine() ?: "").evaluate())
}

abstract class Packet(val version: Long, val type: Long) {
	abstract val length: Long
	abstract val subpackets: List<Packet>
	abstract fun evaluate(): Long
	override fun toString(): String {
		return "Packet(version=$version, type=$type, length=$length, subpackets=$subpackets, value=${evaluate()})"
	}
}

class LiteralPacket(version: Long, type: Long, bits: String) : Packet(version, type) {
	val value: Long
	override val length: Long
	override val subpackets = listOf<Packet>()
	override fun evaluate(): Long {
		return value
	}

	init {
		var start = bits
		var v = 0L
		val mult = 16L
		var len = HEADER_LENGTH.toLong()
		do {
			val chunk = readChunk(start)
			start = start.substring(5)
			v = v * mult + chunk.second
			len += 5
		} while (chunk.first)
		value = v
		length = len
	}

	private fun readChunk(bits: String): Pair<Boolean, Long> {
		val cont = toLong(bits.substring(0, 1))
		val value = toLong(bits.substring(1, 5))
		return Pair(cont == 1L, value)
	}
}

class OperatorPacket(version: Long, type: Long, bits: String) : Packet(version, type) {
	val lengthType: Long
	override val length: Long
	override val subpackets: List<Packet>
	override fun evaluate(): Long {
		return fromCode(type).apply(subpackets)
	}

	enum class Operator(val code: Long) : Function<List<Packet>, Long> {
		SUM(0L) {
			override fun apply(packets: List<Packet>): Long {
				return packets.sumOf(Packet::evaluate)
			}
		},
		PRODUCT(1L) {
			override fun apply(packets: List<Packet>): Long {
				return packets.map(Packet::evaluate).fold(1L) { prod, v -> prod.times(v) }
			}
		},
		MINIMUM(2L) {
			override fun apply(packets: List<Packet>): Long {
				return packets.minOfOrNull(Packet::evaluate) ?: throw IllegalStateException("no min")
			}
		},
		MAXIMUM(3L) {
			override fun apply(packets: List<Packet>): Long {
				return packets.maxOfOrNull(Packet::evaluate) ?: throw IllegalStateException("no max")
			}
		},
		GREATER_THAN(5L) {
			override fun apply(packets: List<Packet>): Long {
				return if (packets.first().evaluate() > packets.last().evaluate()) 1L else 0L
			}
		},
		LESS_THAN(6L) {
			override fun apply(packets: List<Packet>): Long {
				return if (packets.first().evaluate() < packets.last().evaluate()) 1L else 0L
			}
		},
		EQUAL_TO(7L) {
			override fun apply(packets: List<Packet>): Long {
				return if (packets.first().evaluate() == packets.last().evaluate()) 1L else 0L
			}
		}
	}

	private fun fromCode(code: Long): Operator {
		return Operator.values().first { o -> o.code == code }
	}

	init {
		lengthType = toLong(bits.substring(0, 1))
		val limitLength = if (lengthType == 0L) 15 else 11
		val limit = toLong(bits.substring(1, 1 + limitLength))
		var start = bits.substring(1 + limitLength)
		val packs = mutableListOf<Packet>()
		val stop = { count: Long, length: Long -> if (lengthType == 0L) length == limit else count == limit }
		var count = 0L
		var packetsLength = 0L
		while (!stop(count, packetsLength)) {
			val packet = readPacket(start)
			count += 1L
			packetsLength += packet.length
			packs.add(packet)
			start = start.substring(packet.length.toInt())
		}
		length = HEADER_LENGTH + 1 + limitLength + packetsLength
		subpackets = packs
	}
}
