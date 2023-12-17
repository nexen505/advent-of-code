package aoc_2023

import println
import readInput
import toCharArray2
import java.util.LinkedList

private data class State(val i: Int, val j: Int, val dir: Int, val steps: Int, val cost: Long = 0L)

private fun List<String>.toIntArray2() = toCharArray2().map { row ->
    row.map { it.digitToInt() }.toIntArray()
}

private fun findLeastHeatLoss(heatMap: List<IntArray>, minSteps: Int = 1, maxSteps: Int = 3): Long {
    val n = heatMap.size
    val m = heatMap.first().size
    val dp = List(n) { List(m) { List(4) { MutableList(maxSteps + 1) { Long.MAX_VALUE } } } }

    dp[0][0][0][0] = 0
    dp[0][0][1][0] = 0
    dp[0][0][2][0] = 0
    dp[0][0][3][0] = 0

    val queue = LinkedList<State>()
    queue.add(State(0, 0, -1, 0, 0))

    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)

    while (queue.isNotEmpty()) {
        val currentState = queue.removeFirst()
        if (currentState.i == n - 1 && currentState.j == m - 1) {
            continue
        }

        for ((diri, direction) in directions.withIndex()) {
            if (currentState.dir != -1 && ((currentState.dir - diri + directions.size) % directions.size == 2)) {
                continue
            }

            val (dRow, dCol) = direction
            val newi = currentState.i + dRow
            val newj = currentState.j + dCol
            val newSteps = if (currentState.dir == -1 || currentState.dir == diri) currentState.steps + 1 else 1
            val additionalCost = if (newi in 0..<n && newj in 0..<m) heatMap[newi][newj] else 0

            if (newi in 0..<n && newj in 0..<m && newSteps in minSteps..maxSteps) {
                val newCost = currentState.cost + additionalCost

                if (dp[newi][newj][diri][newSteps] > newCost) {
                    dp[newi][newj][diri][newSteps] = newCost

                    queue.add(State(newi, newj, diri, newSteps, newCost))
                }
            }
        }
    }

    return dp[n - 1][m - 1].flatten().min()
}

private fun part1(lines: List<String>): Long {
    val map = lines.toIntArray2()
    val findLeastHeatLoss = findLeastHeatLoss(map, 1, 3)

    return findLeastHeatLoss
}


private fun part2(lines: List<String>): Long {
    val map = lines.toIntArray2()
    val findLeastHeatLoss = findLeastHeatLoss(map, 4, 10)

    return findLeastHeatLoss
}

fun main() {

    val testInput = readInput("aoc_2023/Day17_test")
    val input = readInput("aoc_2023/Day17")

    check(part1(testInput) == 102L)
    part1(input).println()

    check(part2(testInput) == 94L)
    part2(input).println()

}
