package aoc2021.d18

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SnailfishNumberTest {
	@Test
	fun testAdd() {
		assertEquals(
			SnailfishNumber(
				SnailfishNumber(
					RegularNumber(1), RegularNumber(2)
				),
				SnailfishNumber(
					SnailfishNumber(
						RegularNumber(3), RegularNumber(4)
					),
					RegularNumber(5)
				)
			),
			SnailfishNumber(
				RegularNumber(1),
				RegularNumber(2)
			).add(
				SnailfishNumber(
					SnailfishNumber(
						RegularNumber(3),
						RegularNumber(4)
					),
					RegularNumber(5)
				)
			)
		)
	}

	@Test
	fun testParse() {
		assertEquals(
			SnailfishNumber(
				SnailfishNumber(
					RegularNumber(1),
					RegularNumber(-2)
				),
				RegularNumber(3)
			),
			parse("[[1,-2],3]")
		)
	}

	@Test
	fun testExplode1() {
		val number = parse("[[[[[9,8],1],2],3],4]")
		assertTrue(number.explode())
		assertEquals(parse("[[[[0,9],2],3],4]"), number)
	}

	@Test
	fun testExplode2() {
		val number = parse("[7,[6,[5,[4,[3,2]]]]]")
		assertTrue(number.explode())
		assertEquals(parse("[7,[6,[5,[7,0]]]]"), number)
	}

	@Test
	fun testExplode3() {
		val number = parse("[[6,[5,[4,[3,2]]]],1]")
		assertTrue(number.explode())
		assertEquals(parse("[[6,[5,[7,0]]],3]"), number)
	}

	@Test
	fun testExplode4() {
		val number = parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
		assertTrue(number.explode())
		assertEquals(parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"), number)
	}

	@Test
	fun testExplode5() {
		val number = parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
		assertTrue(number.explode())
		assertEquals(parse("[[3,[2,[8,0]]],[9,[5,[7,0]]]]"), number)
	}

	@Test
	fun testSplit1() {
		val number = parse("[[17,10],[4,5]]")
		assertTrue(number.splitTree())
		assertEquals(parse("[[[8,9],10],[4,5]]"), number)
	}

	@Test
	fun testPlus1() {
		val number = (parse("[[[[4,3],4],4],[7,[[8,4],9]]]") + parse("[1,1]"))
		assertEquals(parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"), number)
	}

	@Test
	fun testPlus2() {
		assertEquals(
			parse("[[[[1,1],[2,2]],[3,3]],[4,4]]"),
			"""
			[1,1]
			[2,2]
			[3,3]
			[4,4]				
			""".trimIndent()
				.split("\n")
				.map(String::trim)
				.map(::parse)
				.reduce { n1, n2 -> n1 + n2 }
		)
	}

	@Test
	fun testPlus3() {
		assertEquals(
			parse("[[[[3,0],[5,3]],[4,4]],[5,5]]"),
			"""
			[1,1]
			[2,2]
			[3,3]
			[4,4]
			[5,5]
			""".trimIndent()
				.split("\n")
				.map(String::trim)
				.map(::parse)
				.reduce { n1, n2 -> n1 + n2 }
		)
	}

	@Test
	fun testPlus4() {
		assertEquals(
			parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"),
			"""
			[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
			[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
			[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
			[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
			[7,[5,[[3,8],[1,4]]]]
			[[2,[2,2]],[8,[8,1]]]
			[2,9]
			[1,[[[9,3],9],[[9,0],[0,7]]]]
			[[[5,[7,4]],7],1]
			[[[[4,2],2],6],[8,7]]
			""".trimIndent()
				.split("\n")
				.map(String::trim)
				.map(::parse)
				.reduce { n1, n2 -> n1 + n2 }
		)
	}

	@Test
	fun testMagnitude() {
		assertEquals(3488L, parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude())
	}

	@Test
	fun testExample() {
		assertEquals(
			4140L,
			"""
			[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
			[[[5,[2,8]],4],[5,[[9,9],0]]]
			[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
			[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
			[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
			[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
			[[[[5,4],[7,7]],8],[[8,3],8]]
			[[9,3],[[9,9],[6,[4,9]]]]
			[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
			[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
			""".trimIndent()
				.split("\n")
				.map(String::trim)
				.map(::parse)
				.reduce(SnailfishNumber::plus)
				.magnitude()
		)
	}
}
