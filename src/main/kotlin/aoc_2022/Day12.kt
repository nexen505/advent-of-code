package aoc_2022

import println
import readInput
import size2
import toCharArray2
import java.util.LinkedList

private const val START = 'S'
private const val END = 'E'

private inline fun Array<CharArray>.findDistance(
    start: Pair<Int, Int>,
    isNeighbour: (Char, Char) -> Boolean,
    isDestination: (Pair<Int, Int>) -> Boolean
): Int {
    val (n, m) = size2()
    val queue = LinkedList(listOf(0 to start))
    val visited = Array(n) { BooleanArray(m) }

    val (si, sj) = start
    visited[si][sj] = true

    while (queue.isNotEmpty()) {
        val (dist, cij) = queue.remove()
        val (ci, cj) = cij

        val neighbours = setOf(ci + 1 to cj, ci - 1 to cj, ci to cj + 1, ci to cj - 1)
        for (nij in neighbours) {
            val (ni, nj) = nij

            if (ni in 0..<n && nj in 0..<m && !visited[ni][nj]) {
                val nc = this[ni][nj]
                val c = this[ci][cj]

                if (isNeighbour(c, nc)) {
                    if (isDestination(nij)) {
                        return dist + 1
                    }

                    visited[ni][nj] = true
                    queue += dist + 1 to nij
                }
            }
        }
    }

    return Int.MAX_VALUE
}

/**
 * --- Day 12: Hill Climbing Algorithm ---
 *
 * You try contacting the Elves using your handheld device, but the river you're following must be too low to get a decent signal.
 *
 * You ask the device for a heightmap of the surrounding area (your puzzle input). The heightmap shows the local area from above broken into a grid; the elevation of each square of the grid is given by a single lowercase letter, where a is the lowest elevation, b is the next-lowest, and so on up to the highest elevation, z.
 *
 * Also included on the heightmap are marks for your current position (S) and the location that should get the best signal (E). Your current position (S) has elevation a, and the location that should get the best signal (E) has elevation z.
 *
 * You'd like to reach E, but to save energy, you should do it in as few steps as possible. During each step, you can move exactly one square up, down, left, or right. To avoid needing to get out your climbing gear, the elevation of the destination square can be at most one higher than the elevation of your current square; that is, if your current elevation is m, you could step to elevation n, but not to elevation o. (This also means that the elevation of the destination square can be much lower than the elevation of your current square.)
 *
 * For example:
 *
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 *
 * Here, you start in the top-left corner; your goal is near the middle. You could start by moving down or right, but eventually you'll need to head toward the e at the bottom. From there, you can spiral around to the goal:
 *
 * v..v<<<<
 * >v.vv<<^
 * .>vv>E^^
 * ..v>>>^^
 * ..>>>>>^
 *
 * In the above diagram, the symbols indicate whether the path exits each square moving up (^), down (v), left (<), or right (>). The location that should get the best signal is still E, and . marks unvisited squares.
 *
 * This path reaches the goal in 31 steps, the fewest possible.
 *
 * What is the fewest steps required to move from your current position to the location that should get the best signal?
 *
 */
private fun part1(lines: List<String>): Int {
    val (chars, start, end) = lines.toHills()
    val endDist = chars.findDistance(start, { a, b -> b.code - a.code <= 1 }) { it == end }

    return endDist
}

private fun List<String>.toHills(): Triple<Array<CharArray>, Pair<Int, Int>, Pair<Int, Int>> {
    val chars = toCharArray2()

    val start = chars
        .withIndex()
        .asSequence()
        .map { it to it.value.indexOf(START) }
        .first { it.second >= 0 }
        .let { it.first.index to it.second }
    chars[start.first][start.second] = 'a'

    val end = chars
        .withIndex()
        .asSequence()
        .map { it to it.value.indexOf(END) }
        .first { it.second >= 0 }
        .let { it.first.index to it.second }
    chars[end.first][end.second] = 'z'

    return Triple(chars, start, end)
}

/**
 * --- Part Two ---
 *
 * As you walk up the hill, you suspect that the Elves will want to turn this into a hiking trail. The beginning isn't very scenic, though; perhaps you can find a better starting point.
 *
 * To maximize exercise while hiking, the trail should start as low as possible: elevation a. The goal is still the square marked E. However, the trail should still be direct, taking the fewest steps to reach its goal. So, you'll need to find the shortest path from any square at elevation a to the square marked E.
 *
 * Again consider the example from above:
 *
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 *
 * Now, there are six choices for starting position (five marked a, plus the square marked S that counts as being at elevation a). If you start at the bottom-left square, you can reach the goal most quickly:
 *
 * ...v<<<<
 * ...vv<<^
 * ...v>E^^
 * .>v>>>^^
 * >^>>>>>^
 *
 * This path reaches the goal in only 29 steps, the fewest possible.
 *
 * What is the fewest steps required to move starting from any square with elevation a to the location that should get the best signal?
 *
 */
private fun part2(lines: List<String>): Int {
    val (chars, _, end) = lines.toHills()
    val endDist = chars.findDistance(end, { a, b -> b.code - a.code >= -1 }) { (i, j) -> chars[i][j] == 'a' }

    return endDist
}

fun main() {

    val testInput = readInput("aoc_2022/Day12_test")
    val input = readInput("aoc_2022/Day12")

    check(part1(testInput) == 31)
    part1(input).println()

    check(part2(testInput) == 29)
    part2(input).println()

}
