package aoc_2023

import println
import readInput

private const val DELIMITER = '.'
private const val GEAR = '*'

private fun List<String>.toChar2Array(): Array<CharArray> {
    val n = this.size
    val m = this.first().length
    val chars = Array(n) { CharArray(m) }
    for (i in 0 until n) {
        val line = this[i]

        for (j in 0 until m) {
            chars[i][j] = line[j]
        }
    }

    return chars
}

/**
 * --- Day 3: Gear Ratios ---
 * You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water source, but this is as far as he can bring you. You go inside.
 *
 * It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.
 *
 * "Aaah!"
 *
 * You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone! The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.
 *
 * The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one. If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.
 *
 * The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * Here is an example engine schematic:
 *
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine schematic?
 *
 */
private fun part1(lines: List<String>): Long {
    if (lines.isEmpty()) {
        return 0L
    }

    var sum = 0L

    val chars = lines.toChar2Array()
    val n = chars.size
    val m = chars.first().size

    for (i in 0 until n) {
        var j = 0

        while (j < m) {
            var c = chars[i][j]
            if (!c.isDigit()) {
                ++j
                continue
            }

            val j0 = j
            var num = 0L
            while (c.isDigit()) {
                num = num * 10 + c.digitToInt()
                ++j
                if (j >= m) {
                    break
                }

                c = chars[i][j]
            }

            var isPart = false
            var i1 = i - 1
            while (!isPart && i1 <= i + 1) {
                var j1 = j0 - 1
                while (!isPart && j1 <= j) {
                    if (i1 >= 0 && j1 >= 0 && i1 < n && j1 < m) {
                        val adjc = chars[i1][j1]

                        if (!adjc.isDigit() && adjc != DELIMITER) {
                            isPart = true
                        }
                    }

                    j1++
                }

                i1++
            }

            if (isPart) {
                sum += num
            }
        }
    }

    return sum
}

/**
 * --- Part Two ---
 * The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.
 *
 * You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.
 *
 * Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.
 *
 * The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.
 *
 * This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.
 *
 * Consider the same engine schematic again:
 *
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 * In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.
 *
 * What is the sum of all of the gear ratios in your engine schematic?
 *
 */
private fun part2(lines: List<String>): Long {
    if (lines.isEmpty()) {
        return 0
    }

    val chars = lines.toChar2Array()
    val n = chars.size
    val m = chars.first().size

    val gr = Array(n) { LongArray(m) { 1 } }
    val gc = Array(n) { IntArray(m) }
    for (i in 0 until n) {
        var j = 0

        while (j < m) {
            var c = chars[i][j]
            if (!c.isDigit()) {
                ++j
                continue
            }

            val j0 = j
            var num = 0L
            while (Character.isDigit(c)) {
                num = num * 10 + c.digitToInt()
                ++j
                if (j >= m) {
                    break
                }

                c = chars[i][j]
            }

            for (i1 in i - 1..i + 1) {
                for (j1 in j0 - 1..j) {
                    if (i1 >= 0 && j1 >= 0 && i1 < n && j1 < m && chars[i1][j1] == GEAR) {
                        ++gc[i1][j1]
                        gr[i1][j1] *= num
                    }
                }
            }
        }
    }

    var sum = 0L
    for (i in gc.indices) {
        for (j in gc[i].indices) {
            if (gc[i][j] == 2) {
                sum += gr[i][j]
            }
        }
    }

    return sum
}

fun main() {

    val testInput = readInput("aoc_2023/Day03_test")
    val input = readInput("aoc_2023/Day03")

    check(part1(testInput) == 4361L)
    part1(input).println()

    check(part2(testInput) == 467835L)
    part2(input).println()

}
