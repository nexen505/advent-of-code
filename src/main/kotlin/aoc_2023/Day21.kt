package aoc_2023

import println
import readInput
import size2
import java.util.LinkedList
import kotlin.math.pow

private const val START = 'S'
private const val PLOT = '.'
private const val ROCK = '#'

private fun List<String>.parse(): Pair<Array<BooleanArray>, Pair<Int, Int>> {
    val arr = Array(size) { BooleanArray(first().length) }
    var start: Pair<Int, Int>? = null

    for ((i, line) in this.withIndex()) {
        for ((j, c) in line.withIndex()) {
            when (c) {
                START -> {
                    start = i to j
                    arr[i][j] = false
                }

                ROCK -> {
                    arr[i][j] = true
                }

                PLOT -> {
                    arr[i][j] = false
                }

                else -> error("Unknown character: $c")
            }
        }
    }

    return arr to start!!
}

private fun Array<BooleanArray>.calculateReachedPlots(start: Pair<Int, Int>, steps: Int): Long {
    val (n, m) = size2()
    var plots = linkedSetOf(start)

    var k = 0
    while (k < steps) {
        val newPlots = linkedSetOf<Pair<Int, Int>>()
        while (plots.isNotEmpty()) {
            val p = plots.removeFirst()
            for ((i1, j1) in getNeighbours(p)) {
                if (i1 in 0..<n && j1 in 0..<m && !this[i1][j1]) {
                    newPlots.add(i1 to j1)
                }
            }
        }

        plots = newPlots
        ++k
    }

    return plots.size.toLong()
}

private fun getNeighbours(p: Pair<Int, Int>): List<Pair<Int, Int>> =
    listOf(p.first - 1 to p.second, p.first + 1 to p.second, p.first to p.second - 1, p.first to p.second + 1)

/**
 * --- Day 21: Step Counter ---
 *
 * You manage to catch the airship right as it's dropping someone else off on their all-expenses-paid trip to Desert Island! It even helpfully drops you off near the gardener and his massive farm.
 *
 * "You got the sand flowing again! Great work! Now we just need to wait until we have enough sand to filter the water for Snow Island and we'll have snow again in no time."
 *
 * While you wait, one of the Elves that works with the gardener heard how good you are at solving problems and would like your help. He needs to get his steps in for the day, and so he'd like to know which garden plots he can reach with exactly his remaining 64 steps.
 *
 * He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.), and rocks (#). For example:
 *
 * ...........
 * .....###.#.
 * .###.##..#.
 * ..#.#...#..
 * ....#.#....
 * .##..S####.
 * .##..#...#.
 * .......##..
 * .##.#.####.
 * .##..##.##.
 * ...........
 *
 * The Elf starts at the starting position (S) which also counts as a garden plot. Then, he can take one step north, south, east, or west, but only onto tiles that are garden plots. This would allow him to reach any of the tiles marked O:
 *
 * ...........
 * .....###.#.
 * .###.##..#.
 * ..#.#...#..
 * ....#O#....
 * .##.OS####.
 * .##..#...#.
 * .......##..
 * .##.#.####.
 * .##..##.##.
 * ...........
 *
 * Then, he takes a second step. Since at this point he could be at either tile marked O, his second step would allow him to reach any garden plot that is one step north, south, east, or west of any tile that he could have reached after the first step:
 *
 * ...........
 * .....###.#.
 * .###.##..#.
 * ..#.#O..#..
 * ....#.#....
 * .##O.O####.
 * .##.O#...#.
 * .......##..
 * .##.#.####.
 * .##..##.##.
 * ...........
 *
 * After two steps, he could be at any of the tiles marked O above, including the starting position (either by going north-then-south or by going west-then-east).
 *
 * A single third step leads to even more possibilities:
 *
 * ...........
 * .....###.#.
 * .###.##..#.
 * ..#.#.O.#..
 * ...O#O#....
 * .##.OS####.
 * .##O.#...#.
 * ....O..##..
 * .##.#.####.
 * .##..##.##.
 * ...........
 *
 * He will continue like this until his steps for the day have been exhausted. After a total of 6 steps, he could reach any of the garden plots marked O:
 *
 * ...........
 * .....###.#.
 * .###.##.O#.
 * .O#O#O.O#..
 * O.O.#.#.O..
 * .##O.O####.
 * .##.O#O..#.
 * .O.O.O.##..
 * .##.#.####.
 * .##O.##.##.
 * ...........
 *
 * In this example, if the Elf's goal was to get exactly 6 more steps today, he could use them to reach any of 16 garden plots.
 *
 * However, the Elf actually needs to get 64 steps today, and the map he's handed you is much larger than the example map.
 *
 * Starting from the garden plot marked S on your map, how many garden plots could the Elf reach in exactly 64 steps?
 *
 */
private fun part1(lines: List<String>, steps: Int = 64): Long {
    val (garden, start) = lines.parse()

    return garden.calculateReachedPlots(start, steps)
}

/**
 * --- Part Two ---
 *
 * The Elf seems confused by your answer until he realizes his mistake: he was reading from a list of his favorite numbers that are both perfect squares and perfect cubes, not his step counter.
 *
 * The actual number of steps he needs to get today is exactly 26501365.
 *
 * He also points out that the garden plots and rocks are set up so that the map repeats infinitely in every direction.
 *
 * So, if you were to look one additional map-width or map-height out from the edge of the example map above, you would find that it keeps repeating:
 *
 * .................................
 * .....###.#......###.#......###.#.
 * .###.##..#..###.##..#..###.##..#.
 * ..#.#...#....#.#...#....#.#...#..
 * ....#.#........#.#........#.#....
 * .##...####..##...####..##...####.
 * .##..#...#..##..#...#..##..#...#.
 * .......##.........##.........##..
 * .##.#.####..##.#.####..##.#.####.
 * .##..##.##..##..##.##..##..##.##.
 * .................................
 * .................................
 * .....###.#......###.#......###.#.
 * .###.##..#..###.##..#..###.##..#.
 * ..#.#...#....#.#...#....#.#...#..
 * ....#.#........#.#........#.#....
 * .##...####..##..S####..##...####.
 * .##..#...#..##..#...#..##..#...#.
 * .......##.........##.........##..
 * .##.#.####..##.#.####..##.#.####.
 * .##..##.##..##..##.##..##..##.##.
 * .................................
 * .................................
 * .....###.#......###.#......###.#.
 * .###.##..#..###.##..#..###.##..#.
 * ..#.#...#....#.#...#....#.#...#..
 * ....#.#........#.#........#.#....
 * .##...####..##...####..##...####.
 * .##..#...#..##..#...#..##..#...#.
 * .......##.........##.........##..
 * .##.#.####..##.#.####..##.#.####.
 * .##..##.##..##..##.##..##..##.##.
 * .................................
 *
 * This is just a tiny three-map-by-three-map slice of the inexplicably-infinite farm layout; garden plots and rocks repeat as far as you can see. The Elf still starts on the one middle tile marked S, though - every other repeated S is replaced with a normal garden plot (.).
 *
 * Here are the number of reachable garden plots in this new infinite version of the example map for different numbers of steps:
 *
 *     In exactly 6 steps, he can still reach 16 garden plots.
 *     In exactly 10 steps, he can reach any of 50 garden plots.
 *     In exactly 50 steps, he can reach 1594 garden plots.
 *     In exactly 100 steps, he can reach 6536 garden plots.
 *     In exactly 500 steps, he can reach 167004 garden plots.
 *     In exactly 1000 steps, he can reach 668697 garden plots.
 *     In exactly 5000 steps, he can reach 16733044 garden plots.
 *
 * However, the step count the Elf needs is much larger! Starting from the garden plot marked S on your infinite map, how many garden plots could the Elf reach in exactly 26501365 steps?
 */
private fun part2(lines: List<String>, steps: Int = 26501365): Long {
    val (garden, start) = lines.parse()

    return garden.calculateInfinitelyReachedPlots(start, steps)
}

private fun Array<BooleanArray>.getReached(start: Pair<Int, Int>, size: Int): Long {
    val (n, m) = size2()
    val reached = mutableSetOf<Pair<Int, Int>>()
    val visited = mutableSetOf(start)
    val queue = LinkedList(listOf(start to size))

    do {
        val (current, s) = queue.remove()
        if (s % 2 == 0) {
            reached.add(current)
        }
        if (s == 0) {
            continue
        }

        for ((i, j) in getNeighbours(current)) {
            if (i !in 0..<n || j !in 0..<m || this[i][j] || i to j in visited) {
                continue
            }

            visited.add(i to j)
            queue.add((i to j) to s - 1)
        }
    } while (queue.isNotEmpty())

    return reached.size.toLong()
}

private fun Array<BooleanArray>.calculateInfinitelyReachedPlots(start: Pair<Int, Int>, steps: Int): Long {
    val (n, m) = size2()
    check(n == m)

    check(start.first == size / 2)
    check(start.second == size / 2)
    check(steps % size == size / 2)

    val gridWidth = steps / size - 1
    val odd = (gridWidth / 2 * 2 + 1.0).pow(2.0).toLong()
    val even = ((gridWidth + 1) / 2 * 2.0).pow(2.0).toLong()

    val (i, j) = start

    val oddReached = getReached(i to j, size * 2 + 1)
    val evenReached = getReached(i to j, size * 2)

    val rightBorder = getReached(i to 0, size - 1)
    val leftBorder = getReached(i to size - 1, size - 1)
    val bottomBorder = getReached(0 to j, size - 1)
    val topBorder = getReached(size - 1 to j, size - 1)

    val topLeft = 0 to 0
    val topRight = 0 to size - 1
    val bottomLeft = size - 1 to 0
    val bottomRight = size - 1 to size - 1

    val small = size / 2 - 1
    val smallTopLeft = getReached(topLeft, small)
    val smallTopRight = getReached(topRight, small)
    val smallBottomLeft = getReached(bottomLeft, small)
    val smallBottomRight = getReached(bottomRight, small)

    val large = size * 3 / 2 - 1
    val largeTopLeft = getReached(topLeft, large)
    val largeTopRight = getReached(topRight, large)
    val largeBottomLeft = getReached(bottomLeft, large)
    val largeBottomRight = getReached(bottomRight, large)

    val oddCount = odd * oddReached
    val evenCount = even * evenReached
    val borderCount = topBorder + rightBorder + bottomBorder + leftBorder
    val smallCount = (gridWidth + 1) * (smallTopLeft + smallTopRight + smallBottomLeft + smallBottomRight)
    val largeCount = gridWidth * (largeTopLeft + largeTopRight + largeBottomLeft + largeBottomRight)
    val count = oddCount + evenCount + borderCount + smallCount + largeCount
    return count
}

fun main() {

    val testInput = readInput("aoc_2023/Day21_test")
    val input = readInput("aoc_2023/Day21")

    check(part1(testInput, 3) == 6L)
    check(part1(testInput, 6) == 16L)
    part1(input).println()

    part2(input).println()

}
