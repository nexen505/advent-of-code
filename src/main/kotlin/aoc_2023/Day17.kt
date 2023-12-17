package aoc_2023

import println
import readInput
import toCharArray2
import java.util.PriorityQueue

private fun List<String>.toIntArray2() = toCharArray2().map { row ->
    row.map { it.digitToInt() }.toIntArray()
}

private data class State(val i: Int, val j: Int, val dir: Int, val steps: Int, val cost: Long = 0L)

private fun calculateLeastHeatLoss(heatMap: List<IntArray>, minSteps: Int, maxSteps: Int): Long {
    val n = heatMap.size
    val m = heatMap.first().size

    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
    val dp = Array(n) { Array(m) { Array(directions.size) { LongArray(maxSteps + 1) { Long.MAX_VALUE } } } }
    for (i in directions.indices) {
        dp[0][0][i][0] = 0
    }

    val queue = PriorityQueue<State>(compareBy { it.cost })
    queue.add(State(0, 0, -1, 0, 0))

    while (queue.isNotEmpty()) {
        val (i, j, prevDir, steps, cost) = queue.remove()
        if (i == n - 1 && j == m - 1 && steps >= minSteps) {
            return cost
        }

        for ((nextDir, direction) in directions.withIndex()) {
            // skip if trying to move back to the same direction where we were coming from
            if (prevDir != -1 && (nextDir + directions.size / 2) % directions.size == prevDir) {
                continue
            }

            for (k in 1..maxSteps) {
                val (di, dj) = direction
                val nextI = i + di * k
                val nextJ = j + dj * k
                if (nextI !in 0..<n || nextJ !in 0..<m) {
                    break
                }

                val isSameDirection = prevDir == -1 || prevDir == nextDir
                val nextSteps = k + if (isSameDirection) steps else 0

                if (nextSteps in minSteps..maxSteps) {
                    val additionalCost = (1..k).sumOf { heatMap[i + di * it][j + dj * it].toLong() }
                    val nextCost = cost + additionalCost

                    if (dp[nextI][nextJ][nextDir][nextSteps] > nextCost) {
                        dp[nextI][nextJ][nextDir][nextSteps] = nextCost
                        queue.add(State(nextI, nextJ, nextDir, nextSteps, nextCost))
                    }
                }
            }
        }
    }

    return Long.MAX_VALUE
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
