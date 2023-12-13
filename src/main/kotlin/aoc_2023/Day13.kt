package aoc_2023

import println
import readInput
import transposed

private fun findHorizontalReflectionAxis(chars: Array<CharArray>): Set<Int> {
    val axis = mutableSetOf<Int>()

    for (i in 0..<chars.size - 1) {
        val current = chars[i]
        val next = chars[i + 1]
        if (!current.contentEquals(next)) {
            continue
        }

        val reflectionSize = minOf(chars.size - i - 1, i + 1)
        val topSlice = chars
            .slice(i - reflectionSize + 1..i)
            .reversed()
            .toTypedArray()
        val bottomSlice = chars.sliceArray(i + 1..i + reflectionSize)
        val isReflected = topSlice.contentDeepEquals(bottomSlice)
        if (isReflected) {
            axis.add(i)
        }
    }

    return axis
}

private fun countHorizontalReflections(chars: Array<CharArray>): Long =
    findHorizontalReflectionAxis(chars).sumOf { it + 1L }

private inline fun countReflections(lines: List<String>, counter: (Array<CharArray>) -> Long): Long =
    sequence {
        val pattern = mutableListOf<String>()
        for (line in lines) {
            if (line.isBlank() || line == "\n") {
                yield(pattern)
                pattern.clear()
            } else {
                pattern.add(line)
            }
        }

        if (pattern.isNotEmpty()) {
            yield(pattern)
        }
    }
        .map { pattern -> pattern.map { it.toCharArray() }.toTypedArray() }
        .sumOf { counter(it) }

/**
 * --- Day 13: Point of Incidence ---
 *
 * With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely up to the edge of Lava Island.
 *
 * There's just one problem: you don't see any lava.
 *
 * You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around. After a while, you make your way to a nearby cluster of mountains only to discover that the valley between them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way; perhaps you should head in that direction?
 *
 * As you move through the valley of mirrors, you find that several of them have fallen from the large metal frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's safe to walk or where you're about to run into a mirror.
 *
 * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by carefully analyzing these patterns, you can figure out where the mirrors are!
 *
 * For example:
 *
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *
 * #...##..#
 * #....#..#
 * ..##..###
 * #####.##.
 * #####.##.
 * ..##..###
 * #....#..#
 *
 * To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal line between two rows or across a vertical line between two columns.
 *
 * In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two columns point at the line between the columns:
 *
 * 123456789
 *     ><
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *     ><
 * 123456789
 *
 * In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and can be ignored; every other column has a reflected column within the pattern and must match exactly: column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
 *
 * The second pattern reflects across a horizontal line instead:
 *
 * 1 #...##..# 1
 * 2 #....#..# 2
 * 3 ..##..### 3
 * 4v#####.##.v4
 * 5^#####.##.^5
 * 6 ..##..### 6
 * 7 #....#..# 7
 *
 * This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8, but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches row 7, row 3 matches row 6, and row 4 matches row 5.
 *
 * To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example, the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above it, a total of 405.
 *
 * Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of your notes?
 *
 */
private fun part1(lines: List<String>): Long = countReflections(lines) {
    val horizontalReflections = countHorizontalReflections(it)
    val verticalReflections = countHorizontalReflections(it.transposed())

    100L * horizontalReflections + verticalReflections
}

private fun CharArray.smudglyContentEquals(other: CharArray): Boolean {
    if (this.size != other.size) {
        return false
    }

    val smudgesCount = withIndex().count { (i, c) -> c != other[i] }
    return smudgesCount == 1
}

private fun Array<CharArray>.smudglyContentEquals(other: Array<CharArray>): Boolean {
    if (this.size != other.size) {
        return false
    }
    if (this[0].size != other[0].size) {
        return false
    }

    var smudgesCount = 0L
    for ((i, ri) in withIndex()) {
        for ((j, cj) in ri.withIndex()) {
            if (cj != other[i][j] && ++smudgesCount > 1) {
                return false
            }
        }
    }

    return smudgesCount == 1L
}

private fun findSmudgedHorizontalReflectionAxis(chars: Array<CharArray>): Set<Int> {
    val axis = mutableSetOf<Int>()

    for (i in 0..<chars.size - 1) {
        val current = chars[i]
        val next = chars[i + 1]
        if (!current.contentEquals(next) && !current.smudglyContentEquals(next)) {
            continue
        }

        val reflectionSize = minOf(chars.size - i - 1, i + 1)
        val topSlice = chars
            .slice(i - reflectionSize + 1..i)
            .reversed()
            .toTypedArray()
        val bottomSlice = chars.sliceArray(i + 1..i + reflectionSize)
        val isReflected = topSlice.smudglyContentEquals(bottomSlice)
        if (isReflected) {
            axis.add(i)
        }
    }

    return axis
}

private fun countSmudgedReflections(chars: Array<CharArray>): Pair<Long, Long> {
    val smudgedHorizontalAxis = findSmudgedHorizontalReflectionAxis(chars)
    if (smudgedHorizontalAxis.isNotEmpty()) {
        val normalHorizontalAxis = findHorizontalReflectionAxis(chars)

        if (smudgedHorizontalAxis != normalHorizontalAxis) {
            return smudgedHorizontalAxis.sumOf { it + 1L } to 0L
        }
    }

    val transposed = chars.transposed()
    val smudgedVerticalAxis = findSmudgedHorizontalReflectionAxis(transposed)
    if (smudgedVerticalAxis.isNotEmpty()) {
        val normalVerticalAxis = findHorizontalReflectionAxis(transposed)

        if (smudgedVerticalAxis != normalVerticalAxis) {
            return 0L to smudgedVerticalAxis.sumOf { it + 1L }
        }
    }

    return 0L to 0L
}

/**
 * --- Part Two ---
 *
 * You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully nobody was watching, because that must have been pretty embarrassing.
 *
 * Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one . or # should be the opposite type.
 *
 * In each pattern, you'll need to locate and fix the smudge that causes a different reflection line to be valid. (The old reflection line won't necessarily continue being valid after the smudge is fixed.)
 *
 * Here's the above example again:
 *
 * #.##..##.
 * ..#.##.#.
 * ##......#
 * ##......#
 * ..#.##.#.
 * ..##..##.
 * #.#.##.#.
 *
 * #...##..#
 * #....#..#
 * ..##..###
 * #####.##.
 * #####.##.
 * ..##..###
 * #....#..#
 *
 * The first pattern's smudge is in the top-left corner. If the top-left # were instead ., it would have a different, horizontal line of reflection:
 *
 * 1 ..##..##. 1
 * 2 ..#.##.#. 2
 * 3v##......#v3
 * 4^##......#^4
 * 5 ..#.##.#. 5
 * 6 ..##..##. 6
 * 7 #.#.##.#. 7
 *
 * With the smudge in the top-left corner repaired, a new horizontal line of reflection between rows 3 and 4 now exists. Row 7 has no corresponding reflected row and can be ignored, but every other row matches exactly: row 1 matches row 6, row 2 matches row 5, and row 3 matches row 4.
 *
 * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
 *
 * 1v#...##..#v1
 * 2^#...##..#^2
 * 3 ..##..### 3
 * 4 #####.##. 4
 * 5 #####.##. 5
 * 6 ..##..### 6
 * 7 #....#..# 7
 *
 * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
 *
 * Summarize your notes as before, but instead use the new different reflection lines. In this example, the first pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal line has 1 row above it, summarizing to the value 400.
 *
 * In each pattern, fix the smudge and find the different line of reflection. What number do you get after summarizing the new reflection line in each pattern in your notes?
 */
private fun part2(lines: List<String>): Long = countReflections(lines) {
    val (horizontal, vertical) = countSmudgedReflections(it)

    100 * horizontal + vertical
}

fun main() {

    val testInput = readInput("aoc_2023/Day13_test", false)
    val input = readInput("aoc_2023/Day13", false)

    check(part1(testInput) == 405L)
    part1(input).println()

    check(part2(testInput) == 400L)
    part2(input).println()

}
