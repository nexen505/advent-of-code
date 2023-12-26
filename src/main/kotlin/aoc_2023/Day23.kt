package aoc_2023

import println
import readInput
import size2
import toCharArray2

private const val PATH = '.'
private const val FOREST = '#'
private const val SLOPE_TOP = '^'
private const val SLOPE_RIGHT = '>'
private const val SLOPE_BOTTOM = 'v'
private const val SLOPE_LEFT = '<'

private fun Array<CharArray>.calculateLongestWalk(): Int {
    val start = first()
        .withIndex()
        .single { it.value == PATH }
        .let { 0 to it.index }
    val end = last()
        .withIndex()
        .single { it.value == PATH }
        .let { size - 1 to it.index }

    return calculateLongestWalk(start, end)
}

private fun Array<CharArray>.calculateLongestWalk(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val (n, m) = size2()
    val visited = Array(n) { BooleanArray(m) }

    return calculateLongestPath(start, end, visited, 0, Int.MIN_VALUE) { current ->
        val currentCell = this[current.first][current.second]

        when (currentCell) {
            PATH -> listOf(
                -1 to 0,
                1 to 0,
                0 to -1,
                0 to 1
            ).map { current.first + it.first to current.second + it.second }

            SLOPE_TOP -> listOf(current.first - 1 to current.second)
            SLOPE_RIGHT -> listOf(current.first to current.second + 1)
            SLOPE_BOTTOM -> listOf(current.first + 1 to current.second)
            SLOPE_LEFT -> listOf(current.first to current.second - 1)
            else -> emptyList()
        }
            .filter { it.first in 0..<n && it.second in 0..<m && this[it.first][it.second] != FOREST }
    }
}

private fun Array<CharArray>.calculateLongestPath(
    current: Pair<Int, Int>,
    destination: Pair<Int, Int>,
    visited: Array<BooleanArray>,
    previousPath: Int,
    previousLongestPath: Int,
    neighboursExtractor: Array<CharArray>.(Pair<Int, Int>) -> List<Pair<Int, Int>>
): Int {
    var currentPath = previousPath
    if (current == destination) {
        return maxOf(previousLongestPath, currentPath)
    }

    visited[current.first][current.second] = true

    var longestPath = previousLongestPath
    val neighbours = neighboursExtractor.invoke(this, current)
    for (neighbour in neighbours) {
        if (!visited[neighbour.first][neighbour.second]) {
            ++currentPath

            longestPath = calculateLongestPath(neighbour, destination, visited, currentPath, longestPath, neighboursExtractor)

            --currentPath
        }
    }

    visited[current.first][current.second] = false

    return longestPath
}

/**
 * --- Day 23: A Long Walk ---
 *
 * The Elves resume water filtering operations! Clean water starts flowing over the edge of Island Island.
 *
 * They offer to help you go over the edge of Island Island, too! Just hold on tight to one end of this impossibly long rope and they'll lower you down a safe distance from the massive waterfall you just created.
 *
 * As you finally reach Snow Island, you see that the water isn't really reaching the ground: it's being absorbed by the air itself. It looks like you'll finally have a little downtime while the moisture builds up to snow-producing levels. Snow Island is pretty scenic, even without any snow; why not take a walk?
 *
 * There's a map of nearby hiking trails (your puzzle input) that indicates paths (.), forest (#), and steep slopes (^, >, v, and <).
 *
 * For example:
 *
 * #.#####################
 * #.......#########...###
 * #######.#########.#.###
 * ###.....#.>.>.###.#.###
 * ###v#####.#v#.###.#.###
 * ###.>...#.#.#.....#...#
 * ###v###.#.#.#########.#
 * ###...#.#.#.......#...#
 * #####.#.#.#######.#.###
 * #.....#.#.#.......#...#
 * #.#####.#.#.#########v#
 * #.#...#...#...###...>.#
 * #.#.#v#######v###.###v#
 * #...#.>.#...>.>.#.###.#
 * #####v#.#.###v#.#.###.#
 * #.....#...#...#.#.#...#
 * #.#########.###.#.#.###
 * #...###...#...#...#.###
 * ###.###.#.###v#####v###
 * #...#...#.#.>.>.#.>.###
 * #.###.###.#.###.#.#v###
 * #.....###...###...#...#
 * #####################.#
 *
 * You're currently on the single path tile in the top row; your goal is to reach the single path tile in the bottom row. Because of all the mist from the waterfall, the slopes are probably quite icy; if you step onto a slope tile, your next step must be downhill (in the direction the arrow is pointing). To make sure you have the most scenic hike possible, never step onto the same tile twice. What is the longest hike you can take?
 *
 * In the example above, the longest hike you can take is marked with O, and your starting position is marked S:
 *
 * #S#####################
 * #OOOOOOO#########...###
 * #######O#########.#.###
 * ###OOOOO#OOO>.###.#.###
 * ###O#####O#O#.###.#.###
 * ###OOOOO#O#O#.....#...#
 * ###v###O#O#O#########.#
 * ###...#O#O#OOOOOOO#...#
 * #####.#O#O#######O#.###
 * #.....#O#O#OOOOOOO#...#
 * #.#####O#O#O#########v#
 * #.#...#OOO#OOO###OOOOO#
 * #.#.#v#######O###O###O#
 * #...#.>.#...>OOO#O###O#
 * #####v#.#.###v#O#O###O#
 * #.....#...#...#O#O#OOO#
 * #.#########.###O#O#O###
 * #...###...#...#OOO#O###
 * ###.###.#.###v#####O###
 * #...#...#.#.>.>.#.>O###
 * #.###.###.#.###.#.#O###
 * #.....###...###...#OOO#
 * #####################O#
 *
 * This hike contains 94 steps. (The other possible hikes you could have taken were 90, 86, 82, 82, and 74 steps long.)
 *
 * Find the longest hike you can take through the hiking trails listed on your map. How many steps long is the longest hike?
 *
 */
private fun part1(lines: List<String>): Int {
    val cells = lines.toCharArray2()
    val longestWalk = cells.calculateLongestWalk()

    return longestWalk
}

/**
 * --- Part Two ---
 *
 * As you reach the trailhead, you realize that the ground isn't as slippery as you expected; you'll have no problem climbing up the steep slopes.
 *
 * Now, treat all slopes as if they were normal paths (.). You still want to make sure you have the most scenic hike possible, so continue to ensure that you never step onto the same tile twice. What is the longest hike you can take?
 *
 * In the example above, this increases the longest hike to 154 steps:
 *
 * #S#####################
 * #OOOOOOO#########OOO###
 * #######O#########O#O###
 * ###OOOOO#.>OOO###O#O###
 * ###O#####.#O#O###O#O###
 * ###O>...#.#O#OOOOO#OOO#
 * ###O###.#.#O#########O#
 * ###OOO#.#.#OOOOOOO#OOO#
 * #####O#.#.#######O#O###
 * #OOOOO#.#.#OOOOOOO#OOO#
 * #O#####.#.#O#########O#
 * #O#OOO#...#OOO###...>O#
 * #O#O#O#######O###.###O#
 * #OOO#O>.#...>O>.#.###O#
 * #####O#.#.###O#.#.###O#
 * #OOOOO#...#OOO#.#.#OOO#
 * #O#########O###.#.#O###
 * #OOO###OOO#OOO#...#O###
 * ###O###O#O###O#####O###
 * #OOO#OOO#O#OOO>.#.>O###
 * #O###O###O#O###.#.#O###
 * #OOOOO###OOO###...#OOO#
 * #####################O#
 *
 * Find the longest hike you can take through the surprisingly dry hiking trails listed on your map. How many steps long is the longest hike?
 *
 */
private fun part2(lines: List<String>): Int {
    val cells = lines.toCharArray2()
    val longestHike = cells.calculateLongestHike()

    return longestHike
}

private fun Array<CharArray>.calculateLongestHike(): Int {
    val start = first()
        .withIndex()
        .single { it.value == PATH }
        .let { 0 to it.index }
    val end = last()
        .withIndex()
        .single { it.value == PATH }
        .let { size - 1 to it.index }

    return calculateLongestHike(start, end)
}

private fun Array<CharArray>.calculateLongestHike(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val (n, m) = size2()
    val visited = Array(n) { BooleanArray(m) }

    return calculateLongestPath(start, end, visited, 0, Int.MIN_VALUE) { current ->
        listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            .map { current.first + it.first to current.second + it.second }
            .filter { it.first in 0..<n && it.second in 0..<m && this[it.first][it.second] != FOREST }
    }
}

fun main() {

    val testInput = readInput("aoc_2023/Day23_test")
    val input = readInput("aoc_2023/Day23")

    check(part1(testInput) == 94)
    part1(input).println()

    check(part2(testInput) == 154)
    part2(input).println()

}
