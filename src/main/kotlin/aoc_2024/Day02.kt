package aoc_2024

import println
import readInput

private fun part1(lines: List<String>): Int = lines
    .asSequence()
    .map { it.toLevel() }
    .filter { it.isSafe() }
    .count()

private fun String.toLevel(): List<Int> = splitToSequence(' ')
    .map { it.toInt() }
    .toList()

private fun List<Int>.isSafe(): Boolean =
    zipWithNext().all { it.first - it.second in 1..3 } ||
        zipWithNext().all { it.first - it.second in -3..-1 }

private fun part2(lines: List<String>): Int = lines
    .asSequence()
    .map { it.toLevel() }
    .filter { it.isSafe() || it.isSoftlySafe() }
    .count()

private fun List<Int>.isSoftlySafe(): Boolean = indices.any {
    (subList(0, it) + subList(it + 1, size)).isSafe()
}

fun main() {
    val testInput = readInput("aoc_2024/Day02_test")
    val input = readInput("aoc_2024/Day02")

    check(part1(testInput) == 2)
    part1(input).println()

    check(part2(testInput) == 4)
    part2(input).println()
}
