package aoc_2022

import println
import readInput

private const val ADDX = "addx"
private const val LIT = '#'
private const val DARK = '.'

private inline fun List<String>.forEachCycle(action: (Int, Long) -> Unit) {
    var x = 1L
    var cycleIdx = 0
    for (line in this) {
        val cmd = line.substringBefore(' ')

        action.invoke(cycleIdx, x)
        ++cycleIdx

        if (cmd == ADDX) {
            action.invoke(cycleIdx, x)
            ++cycleIdx
            x += line.substringAfter(' ').toLong()
        }
    }
}

private fun part1(lines: List<String>, cyclesToCheck: Set<Int> = setOf(20, 60, 100, 140, 180, 220)): Long {
    var strength = 0L

    lines.forEachCycle { cycleIdx, x ->
        if ((cycleIdx + 1) in cyclesToCheck) {
            strength += (cycleIdx + 1) * x
        }
    }

    return strength
}

private fun part2(lines: List<String>, factor: Int = 1): List<String> {
    val rowLength = 40
    val crt = CharArray(rowLength * 6) { DARK }

    lines.forEachCycle { cycleIdx, x ->
        if (cycleIdx % rowLength in x - 1..x + 1) {
            crt[cycleIdx] = LIT
        }
    }

    val rows = crt
        .asSequence()
        .flatMap { c -> sequenceOf(*Array(factor) { c }) }
        .chunked(rowLength * factor) { it.joinToString("") }
        .toList()
    return rows
}

fun main() {

    val testInput = readInput("aoc_2022/Day10_test")
    val input = readInput("aoc_2022/Day10")

    check(part1(testInput) == 13140L)
    part1(input).println()

    check(
        part2(testInput) == listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."
        )
    )
    part2(input, 2).forEach { it.println() }

}
