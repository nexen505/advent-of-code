package aoc_2023

import Direction
import calculateInterior
import calculatePerimeter
import println
import readInput
import java.util.SequencedSet

private const val VERTICAL_PIPE = '|'
private const val HORIZONTAL_PIPE = '-'
private const val NORTH_WEST = 'J'
private const val NORTH_EAST = 'L'
private const val SOUTH_WEST = '7'
private const val SOUTH_EAST = 'F'
private const val START = 'S'
private const val GROUND = '.'

private data class Pipes(val lines: List<String>, val start: Pair<Int, Int>) {

    private val size: Int
        get() = lines.size

    fun findBoundary(): SequencedSet<Pair<Long, Long>> {
        val cycle = linkedSetOf(start.first.toLong() to start.second.toLong())
        var current = calculateInitialState()
        var curCoords = start

        do {
            val (nextCoords, nextDir) = when (current) {
                VERTICAL_PIPE to Direction.UP,
                NORTH_WEST to Direction.RIGHT,
                NORTH_EAST to Direction.LEFT -> (curCoords.first - 1 to curCoords.second) to Direction.UP
                VERTICAL_PIPE to Direction.DOWN,
                SOUTH_WEST to Direction.RIGHT,
                SOUTH_EAST to Direction.LEFT -> (curCoords.first + 1 to curCoords.second) to Direction.DOWN
                HORIZONTAL_PIPE to Direction.LEFT,
                NORTH_WEST to Direction.DOWN,
                SOUTH_WEST to Direction.UP -> (curCoords.first to curCoords.second - 1) to Direction.LEFT
                HORIZONTAL_PIPE to Direction.RIGHT,
                NORTH_EAST to Direction.DOWN,
                SOUTH_EAST to Direction.UP -> (curCoords.first to curCoords.second + 1) to Direction.RIGHT
                else -> error("Incorrect state")
            }

            current = lines[nextCoords.first][nextCoords.second] to nextDir
            curCoords = nextCoords
            cycle.add(curCoords.first.toLong() to curCoords.second.toLong())
        } while (current.first != START)

        return cycle
    }

    private fun calculateInitialState(): Pair<Char, Direction> {
        val (i, j) = start
        val (e1, e2) = linkedMapOf(
            Direction.UP to (if (i == 0) GROUND else lines[i - 1][j]),
            Direction.RIGHT to (if (j == size - 1) GROUND else lines[i][j + 1]),
            Direction.DOWN to (if (i == size - 1) GROUND else lines[i + 1][j]),
            Direction.LEFT to (if (j == 0) GROUND else lines[i][j - 1])
        )
            .filter { it.value != GROUND }
            .entries
            .toList()

        return when (e1.key to e2.key) {
            Direction.UP to Direction.RIGHT -> NORTH_EAST to Direction.DOWN
            Direction.RIGHT to Direction.DOWN -> SOUTH_EAST to Direction.UP
            Direction.DOWN to Direction.LEFT -> SOUTH_WEST to Direction.UP
            Direction.UP to Direction.LEFT -> NORTH_WEST to Direction.DOWN
            Direction.UP to Direction.DOWN -> VERTICAL_PIPE to Direction.UP
            Direction.RIGHT to Direction.LEFT -> HORIZONTAL_PIPE to Direction.RIGHT
            else -> error("Incorrect state")
        }
    }

}

private fun List<String>.parsePipes(): Pipes {
    val start = withIndex()
        .map { (i, line) -> i to line.indexOf(START) }
        .first { it.second != -1 }
        .let { (i, j) -> i to j }

    return Pipes(this, start)
}

/**
 * --- Day 10: Pipe Maze ---
 *
 * You use the hang glider to ride the hot air from Desert Island all the way up to the floating metal island. This island is surprisingly cold and there definitely aren't any thermals to glide on, so you leave your hang glider behind.
 *
 * You wander around for a while, but you don't find any people or animals. However, you do occasionally find signposts labeled "Hot Springs" pointing in a seemingly consistent direction; maybe you can find someone at the hot springs and ask them where the desert-machine parts are made.
 *
 * The landscape here is alien; even the flowers and trees are made of metal. As you stop to admire some metal grass, you notice something metallic scurry away in your peripheral vision and jump into a big pipe! It didn't look like any animal you've ever seen; if you want a better look, you'll need to get ahead of it.
 *
 * Scanning the area, you discover that the entire field you're standing on is densely packed with pipes; it was hard to tell at first because they're the same metallic silver color as the "ground". You make a quick sketch of all of the surface pipes you can see (your puzzle input).
 *
 * The pipes are arranged in a two-dimensional grid of tiles:
 *
 *     | is a vertical pipe connecting north and south.
 *     - is a horizontal pipe connecting east and west.
 *     L is a 90-degree bend connecting north and east.
 *     J is a 90-degree bend connecting north and west.
 *     7 is a 90-degree bend connecting south and west.
 *     F is a 90-degree bend connecting south and east.
 *     . is ground; there is no pipe in this tile.
 *     S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
 *
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains the animal is one large, continuous loop.
 *
 * For example, here is a square loop of pipe:
 *
 * .....
 * .F-7.
 * .|.|.
 * .L-J.
 * .....
 *
 * If the animal had entered this loop in the northwest corner, the sketch would instead look like this:
 *
 * .....
 * .S-7.
 * .|.|.
 * .L-J.
 * .....
 *
 * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how the adjacent pipes connect to it.
 *
 * Unfortunately, there are also many pipes that aren't connected to the loop! This sketch shows the same loop as above:
 *
 * -L|F7
 * 7S-7|
 * L|7||
 * -L-J|
 * L|-JF
 *
 * In the above diagram, you can still figure out which pipes form the main loop: they're the ones connected to S, pipes those pipes connect to, pipes those pipes connect to, and so on. Every pipe in the main loop connects to its two neighbors (including S, which will have exactly two pipes connecting to it, and which is assumed to connect back to those two pipes).
 *
 * Here is a sketch that contains a slightly more complex main loop:
 *
 * ..F7.
 * .FJ|.
 * SJ.L7
 * |F--J
 * LJ...
 *
 * Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:
 *
 * 7-F7-
 * .FJ|7
 * SJLL7
 * |F--J
 * LJ.LJ
 *
 * If you want to get out ahead of the animal, you should find the tile in the loop that is farthest from the starting position. Because the animal is in the pipe, it doesn't make sense to measure this by direct distance. Instead, you need to find the tile that would take the longest number of steps along the loop to reach from the starting point - regardless of which way around the loop the animal went.
 *
 * In the first example with the square loop:
 *
 * .....
 * .S-7.
 * .|.|.
 * .L-J.
 * .....
 *
 * You can count the distance each tile in the loop is from the starting point like this:
 *
 * .....
 * .012.
 * .1.3.
 * .234.
 * .....
 *
 * In this example, the farthest point from the start is 4 steps away.
 *
 * Here's the more complex loop again:
 *
 * ..F7.
 * .FJ|.
 * SJ.L7
 * |F--J
 * LJ...
 *
 * Here are the distances for each tile on that loop:
 *
 * ..45.
 * .236.
 * 01.78
 * 14567
 * 23...
 *
 * Find the single giant loop starting at S. How many steps along the loop does it take to get from the starting position to the point farthest from the starting position?
 */
private fun part1(lines: List<String>): Double {
    val pipes = lines.parsePipes()
    val perimeter = pipes.findBoundary().calculatePerimeter()

    return perimeter / 2
}

/**
 * --- Part Two ---
 * You quickly reach the farthest point of the loop, but the animal never emerges. Maybe its nest is within the area enclosed by the loop?
 *
 * To determine whether it's even worth taking the time to search for such a nest, you should calculate how many tiles are contained within the loop. For example:
 *
 * ...........
 * .S-------7.
 * .|F-----7|.
 * .||.....||.
 * .||.....||.
 * .|L-7.F-J|.
 * .|..|.|..|.
 * .L--J.L--J.
 * ...........
 * The above loop encloses merely four tiles - the two pairs of . in the southwest and southeast (marked I below). The middle . tiles (marked O below) are not in the loop. Here is the same loop again with those regions marked:
 *
 * ...........
 * .S-------7.
 * .|F-----7|.
 * .||OOOOO||.
 * .||OOOOO||.
 * .|L-7OF-J|.
 * .|II|O|II|.
 * .L--JOL--J.
 * .....O.....
 * In fact, there doesn't even need to be a full tile path to the outside for tiles to count as outside the loop - squeezing between pipes is also allowed! Here, I is still within the loop and O is still outside the loop:
 *
 * ..........
 * .S------7.
 * .|F----7|.
 * .||OOOO||.
 * .||OOOO||.
 * .|L-7F-J|.
 * .|II||II|.
 * .L--JL--J.
 * ..........
 * In both of the above examples, 4 tiles are enclosed by the loop.
 *
 * Here's a larger example:
 *
 * .F----7F7F7F7F-7....
 * .|F--7||||||||FJ....
 * .||.FJ||||||||L7....
 * FJL7L7LJLJ||LJ.L-7..
 * L--J.L7...LJS7F-7L7.
 * ....F-J..F7FJ|L7L7L7
 * ....L7.F7||L7|.L7L7|
 * .....|FJLJ|FJ|F7|.LJ
 * ....FJL-7.||.||||...
 * ....L---J.LJ.LJLJ...
 * The above sketch has many random bits of ground, some of which are in the loop (I) and some of which are outside it (O):
 *
 * OF----7F7F7F7F-7OOOO
 * O|F--7||||||||FJOOOO
 * O||OFJ||||||||L7OOOO
 * FJL7L7LJLJ||LJIL-7OO
 * L--JOL7IIILJS7F-7L7O
 * OOOOF-JIIF7FJ|L7L7L7
 * OOOOL7IF7||L7|IL7L7|
 * OOOOO|FJLJ|FJ|F7|OLJ
 * OOOOFJL-7O||O||||OOO
 * OOOOL---JOLJOLJLJOOO
 * In this larger example, 8 tiles are enclosed by the loop.
 *
 * Any tile that isn't part of the main loop can count as being enclosed by the loop. Here's another example with many bits of junk pipe lying around that aren't connected to the main loop at all:
 *
 * FF7FSF7F7F7F7F7F---7
 * L|LJ||||||||||||F--J
 * FL-7LJLJ||||||LJL-77
 * F--JF--7||LJLJ7F7FJ-
 * L---JF-JLJ.||-FJLJJ7
 * |F|F-JF---7F7-L7L|7|
 * |FFJF7L7F-JF7|JL---7
 * 7-L-JL7||F7|L7F-7F7|
 * L.L7LFJ|||||FJL7||LJ
 * L7JLJL-JLJLJL--JLJ.L
 * Here are just the tiles that are enclosed by the loop marked with I:
 *
 * FF7FSF7F7F7F7F7F---7
 * L|LJ||||||||||||F--J
 * FL-7LJLJ||||||LJL-77
 * F--JF--7||LJLJIF7FJ-
 * L---JF-JLJIIIIFJLJJ7
 * |F|F-JF---7IIIL7L|7|
 * |FFJF7L7F-JF7IIL---7
 * 7-L-JL7||F7|L7F-7F7|
 * L.L7LFJ|||||FJL7||LJ
 * L7JLJL-JLJLJL--JLJ.L
 * In this last example, 10 tiles are enclosed by the loop.
 *
 * Figure out whether you have time to search for the nest by calculating the area within the loop. How many tiles are enclosed by the loop?
 */
private fun part2(lines: List<String>): Double {
    val pipes = lines.parsePipes()
    val boundary = pipes.findBoundary()

    return boundary.calculateInterior()
}

fun main() {

    val testInput1 = readInput("aoc_2023/Day10_test")
    val testInput2 = readInput("aoc_2023/Day10_test2")
    val input = readInput("aoc_2023/Day10")

    check(part1(testInput1) == 4.0)
    check(part1(testInput2) == 8.0)
    part1(input).println()

    check(part2(testInput1) == 1.0)
    check(part2(testInput2) == 1.0)
    part2(input).println()

}
