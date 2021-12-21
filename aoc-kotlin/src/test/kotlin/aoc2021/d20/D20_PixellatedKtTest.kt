package aoc2021.d20

import java.io.InputStreamReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class D20_PixellatedKtTest {

	private var lines: List<String> = emptyList()

	@BeforeEach
	fun setUp() {
		lines = InputStreamReader(
			D20_PixellatedKtTest::class.java.getResourceAsStream("example.txt")
		).use(InputStreamReader::readLines)
	}

	@Test
	fun testPixelValue() {
		val imageText = """
			...
			#..
			.#.
		""".trimIndent()
		val image = parseImage(imageText.split('\n').map(String::trim))
		assertEquals(34, image.getValue(1, 1))
	}

	@Test
	fun testAlgorithm() {
		val algorithm = parseAlgorithm(lines.first())
		assertFalse(algorithm[0])
		assertFalse(algorithm[1])
		assertTrue(algorithm[2])
		assertTrue(algorithm[34])
	}

	@Test
	fun testListCount() {
		val algorithm = parseAlgorithm(lines.first())
		val image = parseImage(lines.subList(2, lines.size))
		assertEquals(10, image.litCount())
		assertEquals(35, image.step(algorithm, 2).litCount())
		val finalImage = image.step(algorithm, 50)
		println(finalImage)
		assertEquals(3351, finalImage.litCount())
	}
}
