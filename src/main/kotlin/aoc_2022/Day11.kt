package aoc_2022

import lcm
import mul
import println
import readInput

private const val OLD = "old"
private const val PLUS = "+"
private const val MUL = "*"

private data class Monkey(
    val idx: Int,
    val items: MutableList<Long>,
    val op: (Long) -> Long,
    val test: Long,
    val trueTest: Int,
    val falseTest: Int
)

private fun String.parseOp(): (Long) -> Long {
    val opStr = substringAfter(':').trim()
    val rightPart = opStr.substringAfter('=').trim()
    val (op1, opC, op2) = rightPart.split(' ')

    return { v ->
        val a = when (op1) {
            OLD -> v
            else -> op1.toLong()
        }
        val b = when (op2) {
            OLD -> v
            else -> op2.toLong()
        }

        when (opC) {
            PLUS -> a + b
            MUL -> a * b
            else -> error("Unknown operation: $opC")
        }
    }
}

private fun List<String>.toMonkey(): Monkey {
    val idx = this[0]
        .substringAfter(' ')
        .substringBefore(':')
        .toInt()
    val items = this[1]
        .substringAfter(':')
        .trim()
        .split(',')
        .map { it.trim().toLong() }
        .toMutableList()
    val op = this[2].parseOp()
    val test = this[3]
        .substringAfterLast(' ')
        .trim()
        .toLong()
    val trueTest = this[4]
        .substringAfterLast(' ')
        .trim()
        .toInt()
    val falseTest = this[5]
        .substringAfterLast(' ')
        .trim()
        .toInt()

    return Monkey(idx, items, op, test, trueTest, falseTest)
}

private fun List<String>.toMonkeys(): Map<Int, Monkey> {
    val monkeys = mutableMapOf<Int, Monkey>()
    val monkeyLines = mutableListOf<String>()
    for (line in this) {
        if (line.isBlank()) {
            val monkey = monkeyLines.toMonkey()

            monkeys[monkey.idx] = monkey
            monkeyLines.clear()
        } else {
            monkeyLines += line
        }
    }
    if (monkeyLines.isNotEmpty()) {
        val monkey = monkeyLines.toMonkey()

        monkeys[monkey.idx] = monkey
    }

    return monkeys
}

private fun part1(lines: List<String>): Long {
    val monkeys = lines.toMonkeys().toMutableMap()

    return goRounds(monkeys, 20) { it / 3 }
}

private inline fun goRounds(monkeys: MutableMap<Int, Monkey>, rounds: Int, levelOp: (Long) -> Long): Long {
    val monkeyLevels = (0..<monkeys.size)
        .associateWith { 0L }
        .toMutableMap()
    repeat(rounds) {
        for (monkey in monkeys.values) {
            for (item in monkey.items) {
                val level = levelOp.invoke(monkey.op.invoke(item))
                val destMonkeyIdx = if (level % monkey.test == 0L) monkey.trueTest else monkey.falseTest
                val destMonkey = monkeys[destMonkeyIdx]!!
                destMonkey.items += level

                monkeyLevels.merge(monkey.idx, 1L) { a, b -> a + b }
            }

            monkey.items.clear()
        }
    }

    return monkeyLevels
        .values
        .sortedDescending()
        .take(2)
        .mul()
}

private fun part2(lines: List<String>): Long {
    val monkeys = lines.toMonkeys().toMutableMap()
    val mod = monkeys
        .values
        .map { it.test }
        .lcm()

    return goRounds(monkeys, 10000) { it % mod }
}

fun main() {

    val testInput = readInput("aoc_2022/Day11_test", false)
    val input = readInput("aoc_2022/Day11", false)

    check(part1(testInput) == 10605L)
    part1(input).println()

    check(part2(testInput) == 2713310158L)
    part2(input).println()

}
