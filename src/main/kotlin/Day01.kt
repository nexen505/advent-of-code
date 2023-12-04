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
)

fun main() {

    /**
     * --- Day 1: Trebuchet?! ---
     * Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.
     *
     * You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.
     *
     * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
     *
     * You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").
     *
     * As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.
     *
     * The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.
     *
     * For example:
     *
     * 1abc2
     * pqr3stu8vwx
     * a1b2c3d4e5f
     * treb7uchet
     * In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.
     *
     * Consider your entire calibration document. What is the sum of all of the calibration values?
     */
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

    /**
     * --- Part Two ---
     * Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
     *
     * Equipped with this new information, you now need to find the real first and last digit on each line. For example:
     *
     * two1nine
     * eightwothree
     * abcone2threexyz
     * xtwone3four
     * 4nineeightseven2
     * zoneight234
     * 7pqrstsixteen
     * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
     *
     * What is the sum of all of the calibration values?
     *
     */
    fun part2(lines: List<String>): Int = lines.sumOf { line ->
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
