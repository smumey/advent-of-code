package aoc2021.d16

import java.math.BigInteger

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

fun toBigInt(input: String): BigInteger {
	return input.fold(BigInteger.ZERO) { n, bit ->
		n.times(BigInteger.TWO).plus(if (bit == '1') BigInteger.ONE else BigInteger.ZERO)
	}
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
	println(sumVersions(readPacketFromHex(readLine() ?: "")))
}

abstract class Packet(val version: Long, val type: Long) {
	abstract val length: Long
	abstract val subpackets: List<Packet>
	override fun toString(): String {
		return "Packet(version=$version, type=$type, length=$length, subpackets=$subpackets)"
	}
}

class LiteralPacket(version: Long, type: Long, bits: String) : Packet(version, type) {
	val value: BigInteger
	override val length: Long
	override val subpackets = listOf<Packet>()
	override fun toString(): String {
		return super.toString() + " value=" + value
	}

	init {
		var start = bits
		var v = BigInteger.ZERO
		val mult = BigInteger.valueOf(256)
		var len = HEADER_LENGTH.toLong()
		do {
			val chunk = readChunk(start)
			start = start.substring(5)
			v = v * mult + BigInteger.valueOf(chunk.second)
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
