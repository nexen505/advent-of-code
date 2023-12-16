package aoc_2023

import Direction
import println
import readInput
import size2
import toCharArray2
import java.util.EnumSet
import java.util.LinkedList

private const val EMPTY = '.'
private const val RIGHTWARD_MIRROR = '/'
private const val LEFTWARD_MIRROR = '\\'
private const val HORIZONTAL_SPLITTER = '-'
private const val VERTICAL_SPLITTER = '|'

private val GO_UP = setOf(
    Direction.UP to EMPTY,
    Direction.RIGHT to RIGHTWARD_MIRROR,
    Direction.LEFT to LEFTWARD_MIRROR,
    Direction.LEFT to VERTICAL_SPLITTER,
    Direction.RIGHT to VERTICAL_SPLITTER,
    Direction.UP to VERTICAL_SPLITTER
)
private val GO_DOWN = setOf(
    Direction.DOWN to EMPTY,
    Direction.LEFT to RIGHTWARD_MIRROR,
    Direction.RIGHT to LEFTWARD_MIRROR,
    Direction.LEFT to VERTICAL_SPLITTER,
    Direction.RIGHT to VERTICAL_SPLITTER,
    Direction.DOWN to VERTICAL_SPLITTER
)
private val GO_LEFT = setOf(
    Direction.LEFT to EMPTY,
    Direction.DOWN to RIGHTWARD_MIRROR,
    Direction.UP to LEFTWARD_MIRROR,
    Direction.UP to HORIZONTAL_SPLITTER,
    Direction.DOWN to HORIZONTAL_SPLITTER,
    Direction.LEFT to HORIZONTAL_SPLITTER
)
private val GO_RIGHT = setOf(
    Direction.RIGHT to EMPTY,
    Direction.UP to RIGHTWARD_MIRROR,
    Direction.DOWN to LEFTWARD_MIRROR,
    Direction.UP to HORIZONTAL_SPLITTER,
    Direction.DOWN to HORIZONTAL_SPLITTER,
    Direction.RIGHT to HORIZONTAL_SPLITTER
)

private typealias Beam = Triple<Int, Int, Direction>

private fun Array<CharArray>.getNeighbours(pos: Beam): Set<Beam> {
    val neighbours = mutableSetOf<Beam>()

    val (n, m) = size2()
    val (i, j, direction) = pos
    val c = this[i][j]

    if (i > 0 && direction to c in GO_UP) {
        neighbours += Beam(i - 1, j, Direction.UP)
    }
    if (i < n - 1 && direction to c in GO_DOWN) {
        neighbours += Beam(i + 1, j, Direction.DOWN)
    }
    if (j > 0 && direction to c in GO_LEFT) {
        neighbours += Beam(i, j - 1, Direction.LEFT)
    }
    if (j < m - 1 && direction to c in GO_RIGHT) {
        neighbours += Beam(i, j + 1, Direction.RIGHT)
    }

    return neighbours
}

private fun Array<CharArray>.countEnergizedTiles(start: Beam): Long {
    val (n, m) = size2()
    val visited = Array(n) { Array(m) { EnumSet.noneOf(Direction::class.java) } }
    val stack = LinkedList(listOf(start))

    while (stack.isNotEmpty()) {
        val current = stack.removeFirst()
        val (i, j, direction) = current
        val visitedDirections = visited[i][j]

        if (direction !in visitedDirections) {
            visitedDirections += direction

            val neighbours = getNeighbours(current)
            for (neighbour in neighbours) {
                val (ni, nj, ndirection) = neighbour

                if (ndirection !in visited[ni][nj]) {
                    stack += neighbour
                }
            }
        }
    }

    return visited.sumOf { row ->
        val visitedDirections = row.count { it.isNotEmpty() }

        visitedDirections.toLong()
    }
}

/**
 * --- Day 16: The Floor Will Be Lava ---
 * With the beam of light completely focused somewhere, the reindeer leads you deeper still into the Lava Production Facility. At some point, you realize that the steel facility walls have been replaced with cave, and the doorways are just cave, and the floor is cave, and you're pretty sure this is actually just a giant cave.
 *
 * Finally, as you approach what must be the heart of the mountain, you see a bright light in a cavern up ahead. There, you discover that the beam of light you so carefully focused is emerging from the cavern wall closest to the facility and pouring all of its energy into a contraption on the opposite side.
 *
 * Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.), mirrors (/ and \), and splitters (| and -).
 *
 * The contraption is aligned so that most of the beam bounces around the grid, but each tile on the grid converts some of the beam's light into heat to melt the rock in the cavern.
 *
 * You note the layout of the contraption (your puzzle input). For example:
 *
 * .|...\....
 * |.-.\.....
 * .....|-...
 * ........|.
 * ..........
 * .........\
 * ..../.\\..
 * .-.-/..|..
 * .|....-|.\
 * ..//.|....
 * The beam enters in the top-left corner from the left and heading to the right. Then, its behavior depends on what it encounters as it moves:
 *
 * If the beam encounters empty space (.), it continues in the same direction.
 * If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror. For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column, while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
 * If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the splitter were empty space. For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
 * If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing. For instance, a rightward-moving beam that encounters a | splitter would split into two beams: one that continues upward from the splitter's column and one that continues downward from the splitter's column.
 * Beams do not interact with other beams; a tile can have many beams passing through it at the same time. A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.
 *
 * In the above example, here is how the beam of light bounces around the contraption:
 *
 * >|<<<\....
 * |v-.\^....
 * .v...|->>>
 * .v...v^.|.
 * .v...v^...
 * .v...v^..\
 * .v../2\\..
 * <->-/vv|..
 * .|<<<2-|.\
 * .v//.|.v..
 * Beams are only shown on empty tiles; arrows indicate the direction of the beams. If a tile contains beams moving in multiple directions, the number of distinct directions is shown instead. Here is the same diagram but instead only showing whether a tile is energized (#) or not (.):
 *
 * ######....
 * .#...#....
 * .#...#####
 * .#...##...
 * .#...##...
 * .#...##...
 * .#..####..
 * ########..
 * .#######..
 * .#...#.#..
 * Ultimately, in this example, 46 tiles become energized.
 *
 * The light isn't energizing enough tiles to produce lava; to debug the contraption, you need to start by analyzing the current situation. With the beam starting in the top-left heading right, how many tiles end up being energized?
 *
 *
 */
private fun part1(lines: List<String>): Long {
    val chars = lines.toCharArray2()
    val start = Beam(0, 0, Direction.RIGHT)

    return chars.countEnergizedTiles(start)
}

/**
 * --- Part Two ---
 * As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel. There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading away from that edge. (You can choose either of two directions for the beam if it starts on a corner; for instance, if the beam starts in the bottom-right corner, it can start heading either left or upward.)
 *
 * So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward), any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left). To produce lava, you need to find the configuration that energizes as many tiles as possible.
 *
 * In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:
 *
 * .|<2<\....
 * |v-v\^....
 * .v.v.|->>>
 * .v.v.v^.|.
 * .v.v.v^...
 * .v.v.v^..\
 * .v.v/2\\..
 * <-2-/vv|..
 * .|<<<2-|.\
 * .v//.|.v..
 * Using this configuration, 51 tiles are energized:
 *
 * .#####....
 * .#.#.#....
 * .#.#.#####
 * .#.#.##...
 * .#.#.##...
 * .#.#.##...
 * .#.#####..
 * ########..
 * .#######..
 * .#...#.#..
 * Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that configuration?
 */
private fun part2(lines: List<String>): Long {
    val chars = lines.toCharArray2()
    val (n, m) = chars.size2()
    val candidates = (0..<n)
        .toList()
        .asSequence()
        .flatMap { i -> (0..<m).asSequence().map { j -> i to j } }
        .flatMap { (i, j) -> Direction.entries.asSequence().map { direction -> Beam(i, j, direction) } }
        .toList()

    return candidates
        .stream()
        .parallel()
        .mapToLong { chars.countEnergizedTiles(it) }
        .max()
        .orElse(0L)
}

fun main() {

    val testInput = readInput("aoc_2023/Day16_test")
    val input = readInput("aoc_2023/Day16")

    check(part1(testInput) == 46L)
    part1(input).println()

    check(part2(testInput) == 51L)
    part2(input).println()

}
