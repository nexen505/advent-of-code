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
        val (i, j, prevDir, prevSteps, prevCost) = queue.remove()
        if (i == n - 1 && j == m - 1 && prevSteps >= minSteps) {
            return prevCost
        }

        for ((nextDir, direction) in directions.withIndex()) {
            // skip if trying to move back to the same direction where we were coming from
            if (prevDir != -1 && (nextDir + directions.size / 2) % directions.size == prevDir) {
                continue
            }

            val (di, dj) = direction
            val isSameDirection = prevDir == -1 || prevDir == nextDir
            val currentSteps = if (isSameDirection) prevSteps else 0

            var nextI = i
            var nextJ = j
            var additionalSteps = 1
            var additionalCost = 0L
            while (additionalSteps <= maxSteps) {
                nextI += di
                nextJ += dj
                if (nextI !in 0..<n || nextJ !in 0..<m) {
                    break
                }

                additionalCost += heatMap[nextI][nextJ]

                val nextSteps = currentSteps + additionalSteps
                if (nextSteps in minSteps..maxSteps) {
                    val nextCost = prevCost + additionalCost

                    if (dp[nextI][nextJ][nextDir][nextSteps] > nextCost) {
                        dp[nextI][nextJ][nextDir][nextSteps] = nextCost
                        queue.add(State(nextI, nextJ, nextDir, nextSteps, nextCost))
                    }
                }

                ++additionalSteps
            }
        }
    }

    return Long.MAX_VALUE
}

/**
 * --- Day 17: Clumsy Crucible ---
 *
 * The lava starts flowing rapidly once the Lava Production Facility is operational. As you leave, the reindeer offers you a parachute, allowing you to quickly reach Gear Island.
 *
 * As you descend, your bird's-eye view of Gear Island reveals why you had trouble finding anyone on your way up: half of Gear Island is empty, but the half below you is a giant factory city!
 *
 * You land near the gradually-filling pool of lava at the base of your new lavafall. Lavaducts will eventually carry the lava throughout the city, but to make use of it immediately, Elves are loading it into large crucibles on wheels.
 *
 * The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very difficult to steer at high speeds, and so it can be hard to go in a straight line for very long.
 *
 * To get Desert Island the machine parts it needs as soon as possible, you'll need to find the best way to get the crucible from the lava pool to the machine parts factory. To do this, you need to minimize heat loss while choosing a route that doesn't require the crucible to go in a straight line for too long.
 *
 * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient temperature, and hundreds of other parameters to calculate exactly how much heat loss can be expected for a crucible entering any particular city block.
 *
 * For example:
 *
 * 2413432311323
 * 3215453535623
 * 3255245654254
 * 3446585845452
 * 4546657867536
 * 1438598798454
 * 4457876987766
 * 3637877979653
 * 4654967986887
 * 4564679986453
 * 1224686865563
 * 2546548887735
 * 4322674655533
 *
 * Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that block. The starting point, the lava pool, is the top-left city block; the destination, the machine parts factory, is the bottom-right city block. (Because you already start in the top-left block, you don't incur that block's heat loss unless you leave that block and then return to it.)
 *
 * Because it is difficult to keep the top-heavy crucible going in a straight line for very long, it can move at most three blocks in a single direction before it must turn 90 degrees left or right. The crucible also can't reverse direction; after entering each city block, it may only turn left, continue straight, or turn right.
 *
 * One way to minimize heat loss is this path:
 *
 * 2>>34^>>>1323
 * 32v>>>35v5623
 * 32552456v>>54
 * 3446585845v52
 * 4546657867v>6
 * 14385987984v4
 * 44578769877v6
 * 36378779796v>
 * 465496798688v
 * 456467998645v
 * 12246868655<v
 * 25465488877v5
 * 43226746555v>
 *
 * This path never moves more than three consecutive blocks in the same direction and incurs a heat loss of only 102.
 *
 * Directing the crucible from the lava pool to the machine parts factory, but not moving more than three consecutive blocks in the same direction, what is the least heat loss it can incur?
 *
 */
private fun part1(lines: List<String>): Long {
    val map = lines.toIntArray2()
    val loss = calculateLeastHeatLoss(map, 1, 3)

    return loss
}

/**
 * --- Part Two ---
 *
 * The crucibles of lava simply aren't large enough to provide an adequate supply of lava to the machine parts factory. Instead, the Elves are going to upgrade to ultra crucibles.
 *
 * Ultra crucibles are even more difficult to steer than normal crucibles. Not only do they have trouble going in a straight line, but they also have trouble turning!
 *
 * Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks in that direction before it can turn (or even before it can stop at the end). However, it will eventually start to get wobbly: an ultra crucible can move a maximum of ten consecutive blocks without turning.
 *
 * In the above example, an ultra crucible could follow this path to minimize heat loss:
 *
 * 2>>>>>>>>1323
 * 32154535v5623
 * 32552456v4254
 * 34465858v5452
 * 45466578v>>>>
 * 143859879845v
 * 445787698776v
 * 363787797965v
 * 465496798688v
 * 456467998645v
 * 122468686556v
 * 254654888773v
 * 432267465553v
 *
 * In the above example, an ultra crucible would incur the minimum possible heat loss of 94.
 *
 * Here's another example:
 *
 * 111111111111
 * 999999999991
 * 999999999991
 * 999999999991
 * 999999999991
 *
 * Sadly, an ultra crucible would need to take an unfortunate path like this one:
 *
 * 1>>>>>>>1111
 * 9999999v9991
 * 9999999v9991
 * 9999999v9991
 * 9999999v>>>>
 *
 * This route causes the ultra crucible to incur the minimum possible heat loss of 71.
 *
 * Directing the ultra crucible from the lava pool to the machine parts factory, what is the least heat loss it can incur?
 *
 */
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
