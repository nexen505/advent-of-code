import java.util.*

const val VERTICAL_PIPE = '|'
const val HORIZONTAL_PIPE = '-'
const val NORTH_EAST = 'L'
const val NORTH_WEST = 'J'
const val SOUTH_WEST = '7'
const val SOUTH_EAST = 'F'
const val START = 'S'
const val GROUND = '.'

fun main() {

    data class Graph(val adj: Array<LinkedHashSet<Int>>, val start: Int) {

        fun findCycle(): Set<Int> {
            val visited = BooleanArray(adj.size * adj.size) { it == start }
            val parent = IntArray(visited.size) { -1 }
            val stack = LinkedList(listOf(start))

            while (stack.isNotEmpty()) {
                val u = stack.poll()

                for (v in adj[u]) {
                    if (!visited[v]) {
                        visited[v] = true
                        stack.add(v)
                        parent[v] = u
                    } else if (parent[u] != v) {
                        val cycle = parent
                            .asSequence()
                            .withIndex()
                            .filter { it.value != -1 }
                            .map { it.index }
                            .toMutableSet()

                        cycle.add(start)

                        return cycle
                    }
                }
            }

            error("There is not cycles")
        }

        fun findCycleLength(): Int {
            val cycle = findCycle()

            return cycle.size
        }

    }

    fun Pair<Int, Int>.flatten(size: Int): Int = first * size + second

    fun List<String>.toGraph(): Graph {
        val adj = Array(size * size) { linkedSetOf<Int>() }
        var start = 0

        for (i in indices) {
            for ((j, c) in this[i].withIndex()) {
                val current = (i to j).flatten(size)

                when (c) {
                    VERTICAL_PIPE -> {
                        if (i >= 1 && this[i - 1][j] in setOf(VERTICAL_PIPE, SOUTH_WEST, SOUTH_EAST, START)) {
                            adj[(i - 1 to j).flatten(size)].add(current)
                            adj[current].add((i - 1 to j).flatten(size))
                        }
                        if (i <= size - 2 && this[i + 1][j] in setOf(VERTICAL_PIPE, NORTH_WEST, NORTH_EAST, START)) {
                            adj[(i + 1 to j).flatten(size)].add(current)
                            adj[current].add((i + 1 to j).flatten(size))
                        }
                    }

                    HORIZONTAL_PIPE -> {
                        if (j >= 1 && this[i][j - 1] in setOf(HORIZONTAL_PIPE, NORTH_EAST, SOUTH_EAST, START)) {
                            adj[(i to j - 1).flatten(size)].add(current)
                            adj[current].add((i to j - 1).flatten(size))
                        }
                        if (j <= size - 2 && this[i][j + 1] in setOf(HORIZONTAL_PIPE, NORTH_WEST, SOUTH_WEST, START)) {
                            adj[(i to j + 1).flatten(size)].add(current)
                            adj[current].add((i to j + 1).flatten(size))
                        }
                    }

                    NORTH_EAST -> {
                        if (i >= 1 && this[i - 1][j] in setOf(VERTICAL_PIPE, SOUTH_WEST, SOUTH_EAST, START)) {
                            adj[(i - 1 to j).flatten(size)].add(current)
                            adj[current].add((i - 1 to j).flatten(size))
                        }
                        if (j <= size - 2 && this[i][j + 1] in setOf(HORIZONTAL_PIPE, NORTH_WEST, SOUTH_WEST, START)) {
                            adj[(i to j + 1).flatten(size)].add(current)
                            adj[current].add((i to j + 1).flatten(size))
                        }
                    }

                    NORTH_WEST -> {
                        if (i >= 1 && this[i - 1][j] in setOf(VERTICAL_PIPE, SOUTH_WEST, SOUTH_EAST, START)) {
                            adj[(i - 1 to j).flatten(size)].add(current)
                            adj[current].add((i - 1 to j).flatten(size))
                        }
                        if (j >= 1 && this[i][j - 1] in setOf(HORIZONTAL_PIPE, NORTH_EAST, SOUTH_EAST, START)) {
                            adj[(i to j - 1).flatten(size)].add(current)
                            adj[current].add((i to j - 1).flatten(size))
                        }
                    }

                    SOUTH_EAST -> {
                        if (i <= size - 2 && this[i + 1][j] in setOf(VERTICAL_PIPE, NORTH_WEST, NORTH_EAST, START)) {
                            adj[(i + 1 to j).flatten(size)].add(current)
                            adj[current].add((i + 1 to j).flatten(size))
                        }
                        if (j <= size - 2 && this[i][j + 1] in setOf(HORIZONTAL_PIPE, NORTH_WEST, SOUTH_WEST, START)) {
                            adj[(i to j + 1).flatten(size)].add(current)
                            adj[current].add((i to j + 1).flatten(size))
                        }
                    }

                    SOUTH_WEST -> {
                        if (i <= size - 2 && this[i + 1][j] in setOf(VERTICAL_PIPE, NORTH_WEST, NORTH_EAST, START)) {
                            adj[(i + 1 to j).flatten(size)].add(current)
                            adj[current].add((i + 1 to j).flatten(size))
                        }
                        if (j >= 1 && this[i][j - 1] in setOf(HORIZONTAL_PIPE, NORTH_EAST, SOUTH_EAST, START)) {
                            adj[(i to j - 1).flatten(size)].add(current)
                            adj[current].add((i to j - 1).flatten(size))
                        }
                    }

                    START -> {
                        start = current
                    }

                    else -> {}
                }
            }
        }

        return Graph(adj, start)
    }

    fun part1(lines: List<String>): Int {
        val graph = lines.toGraph()
        val cycleLength = graph.findCycleLength()

        return cycleLength / 2
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
    fun part2(lines: List<String>): Int {
        val graph = lines.toGraph()
        val cycle = graph.findCycle()
        val excluded = cycle.toMutableSet()
        val size = lines.size

        for ((i, line) in lines.withIndex()) {
            val fixedLine = (0..<size).joinToString("") { j ->
                val cur = (i to j).flatten(size)

                if (cur in cycle) line[j].toString() else GROUND.toString()
            }
            var isInside = false
            var up: Boolean? = null

            for ((j, c) in fixedLine.withIndex()) {
                when (c) {
                    VERTICAL_PIPE -> {
                        isInside = !isInside
                    }

                    NORTH_EAST, SOUTH_EAST -> {
                        up = c == NORTH_EAST
                    }

                    NORTH_WEST, SOUTH_WEST -> {
                        val expected = if (up != null && up) NORTH_WEST else SOUTH_WEST
                        if (c != expected) {
                            isInside = !isInside
                        }
                        up = null
                    }
                }

                if (!isInside) {
                    excluded.add((i to j).flatten(size))
                }
            }
        }

        return size * size - excluded.size
    }

    val testInput1 = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val input1 = readInput("Day10")
    val input2 = readInput("Day10")

    check(part1(testInput1) == 4)
    check(part1(testInput2) == 8)
    part1(input1).println()

    check(part2(testInput1) == 1)
    check(part2(testInput2) == 1)
    check(part2(testInput3) == 4)
    part2(input2).println()
}
