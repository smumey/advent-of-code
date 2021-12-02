main()

fun main() {
    println(
        generateSequence(::readLine)
        .map(Integer::parseInt)
        .fold(
            intArrayOf(0, Integer.MAX_VALUE),
            { acc: IntArray, i: Int -> if (i > acc[1]) intArrayOf(acc[0] + 1, i) else intArrayOf(acc[0], i) }
        )[0]
    )
}