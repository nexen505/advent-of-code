package aoc_2024

import println
import readInput
import kotlin.math.pow

private enum class Op {
    PLUS, MUL, CONCAT
}

private fun part1(lines: List<String>): Long =
    lines.asSequence().map { it.parse() }.filter { it.evaluated(setOf(Op.PLUS, Op.MUL)) }
        .sumOf { it.first }

private fun String.parse(): Pair<Long, List<Long>> = split(": ").let { (left, right) ->
    left.toLong() to right.splitToSequence(' ').map { it.toLong() }.toList()
}

private fun Pair<Long, List<Long>>.evaluated(ops: Set<Op>): Boolean {
    val (res, vals) = this

    val opsList = ops.toList()
    val combs = ops.size.toDouble().pow(vals.size - 1).toLong()
    for (i in 0 until combs) {
        val expr = i.toUInt().toString(ops.size).padStart(vals.size - 1, '0')
            .map { opsList[it.digitToInt()] }

        var acc = vals.first()
        for (j in 1..<vals.size) {
            when (expr[j - 1]) {
                Op.PLUS -> acc += vals[j]
                Op.MUL -> acc *= vals[j]
                Op.CONCAT -> acc = "$acc${vals[j]}".toLong()
            }
        }

        if (acc == res) {
            return true
        }
    }

    return false
}

private fun part2(lines: List<String>): Long =
    lines.asSequence().map { it.parse() }.filter { it.evaluated(Op.entries.toSet()) }
        .sumOf { it.first }

fun main() {
    val testInput = readInput("aoc_2024/Day07_test")
    val input = readInput("aoc_2024/Day07")

    check(part1(testInput) == 3749L)
    part1(input).println()

    check(part2(testInput) == 11387L)
    part2(input).println()
}
