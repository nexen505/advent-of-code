package aoc_2023

import println
import readInput
import toCharArray2
import java.util.LinkedList

private fun List<String>.toIntArray2() = toCharArray2().map { row ->
    row.map { it.digitToInt() }.toIntArray()
}

private data class State(val i: Int, val j: Int, val dir: Int, val steps: Int, val cost: Long = 0L)

private fun calculateLeastHeatLoss(heatMap: List<IntArray>, minSteps: Int, maxSteps: Int): Long {
    val n = heatMap.size
    val m = heatMap.first().size

    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
    val dp = Array(n) { Array(m) { Array(directions.size) { LongArray(maxSteps + 1) { Long.MAX_VALUE } } } }
    dp[0][0][0][0] = 0
    dp[0][0][1][0] = 0
    dp[0][0][2][0] = 0
    dp[0][0][3][0] = 0

    val queue = LinkedList<State>()
    queue.add(State(0, 0, -1, 0, 0))

    while (queue.isNotEmpty()) {
        val (i, j, prevDir, steps, cost) = queue.removeFirst()
        if (i == n - 1 && j == m - 1) {
            continue
        }

        for ((newDir, direction) in directions.withIndex()) {
            if (prevDir != -1 && ((prevDir - newDir + directions.size) % directions.size == 2)) {
                continue
            }

            val (di, dj) = direction
            for (k in 1..maxSteps) {
                val newi = i + di * k
                val newj = j + dj * k
                if (newi in 0..<n && newj in 0..<m) {
                    val isSameDirection = prevDir == -1 || prevDir == newDir
                    val newSteps = k + if (isSameDirection) steps else 0

                    if (newSteps in minSteps..maxSteps) {
                        val additionalCost = (1..k).sumOf { heatMap[i + di * it][j + dj * it] }
                        val newCost = cost + additionalCost

                        if (dp[newi][newj][newDir][newSteps] > newCost) {
                            dp[newi][newj][newDir][newSteps] = newCost

                            queue.add(State(newi, newj, newDir, newSteps, newCost))
                        }
                    }
                }
            }
        }
    }

    return dp.last().last().minOf { it.min() }
}

private fun part1(lines: List<String>): Long {
    val map = lines.toIntArray2()
    val loss = calculateLeastHeatLoss(map, 1, 3)

    return loss
}


private fun part2(lines: List<String>): Long {
    val map = lines.toIntArray2()
    val loss = calculateLeastHeatLoss(map, 4, 10)

    return loss
}

fun main() {

    val testInput = readInput("aoc_2023/Day17_test")
    val input = readInput("aoc_2023/Day17")

    check(part1(testInput) == 102L)
    part1(input).println()

    check(part2(testInput) == 94L)
    part2(input).println()

}
