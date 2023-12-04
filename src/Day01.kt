fun main() {

    val wordDigits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    );

    fun part1(lines: List<String>): Int = lines.sumOf { line ->
        val length = line.length
        var firstDigit = -1
        var lastDigit = -1
        var i = 0
        do {
            val ci = line[i]
            if (firstDigit < 0 && ci.isDigit()) {
                firstDigit = ci.digitToInt()
            }

            val cii = line[length - 1 - i]
            if (lastDigit < 0 && cii.isDigit()) {
                lastDigit = cii.digitToInt()
            }
        } while (++i < length && (firstDigit < 0 || lastDigit < 0))

        "$firstDigit$lastDigit".toInt()
    }

    fun part2(input: List<String>): Int = input.sumOf { line ->
        val length = line.length
        val digits = mutableListOf<Int>()
        var i = 0

        do {
            val ci = line[i]

            if (ci.isDigit()) {
                digits.add(ci.digitToInt())
            } else {
                for ((digitString, digitValue) in wordDigits) {
                    if (line.startsWith(digitString, i)) {
                        digits.add(digitValue)
                    }
                }
            }
        } while (++i < length)

        "${digits.first()}${digits.last()}".toInt()
    }

    val testInput1 = readInput("Day01_test")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
