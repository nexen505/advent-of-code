package aoc_2022

import println
import readInput

private const val AIR = '.'
private const val ROCK = '#'
private const val SAND = 'o'

private fun List<String>.toRocks(): Set<Pair<Int, Int>> = map { s ->
    s.split("->")
        .map { it.trim() }
        .map {
            val (l, r) = it.split(',')

            l.toInt() to r.toInt()
        }
}
    .flatMap { l ->
        val rocks = l
            .windowed(2, partialWindows = false)
            .map { (a, b) ->
                val (ai, aj) = a
                val (bi, bj) = b

                (minOf(ai, bi)..maxOf(ai, bi))
                    .map { aik ->
                        (minOf(aj, bj)..maxOf(aj, bj)).map { ajk -> ajk to aik }
                    }
                    .flatten()
            }

        rocks.flatten()
    }
    .toSet()

/**
 * --- Day 14: Regolith Reservoir ---
 *
 * The distress signal leads you to a giant waterfall! Actually, hang on - the signal seems like it's coming from the waterfall itself, and that doesn't make any sense. However, you do notice a little path that leads behind the waterfall.
 *
 * Correction: the distress signal leads you behind a giant waterfall! There seems to be a large cave system here, and the signal definitely leads further inside.
 *
 * As you begin to make your way deeper underground, you feel the ground rumble for a moment. Sand begins pouring into the cave! If you don't quickly figure out where the sand is going, you could quickly become trapped!
 *
 * Fortunately, your familiarity with analyzing the path of falling material will come in handy here. You scan a two-dimensional vertical slice of the cave above you (your puzzle input) and discover that it is mostly air with structures made of rock.
 *
 * Your scan traces the path of each solid rock structure and reports the x,y coordinates that form the shape of the path, where x represents distance to the right and y represents distance down. Each path appears as a single line of text in your scan. After the first point of each path, each point indicates the end of a straight horizontal or vertical line to be drawn from the previous point. For example:
 *
 * 498,4 -> 498,6 -> 496,6
 * 503,4 -> 502,4 -> 502,9 -> 494,9
 *
 * This scan means that there are two paths of rock; the first path consists of two straight lines, and the second path consists of three straight lines. (Specifically, the first path consists of a line of rock from 498,4 through 498,6 and another line of rock from 498,6 through 496,6.)
 *
 * The sand is pouring into the cave from point 500,0.
 *
 * Drawing rock as #, air as ., and the source of the sand as +, this becomes:
 *
 *
 *   4     5  5
 *   9     0  0
 *   4     0  3
 * 0 ......+...
 * 1 ..........
 * 2 ..........
 * 3 ..........
 * 4 ....#...##
 * 5 ....#...#.
 * 6 ..###...#.
 * 7 ........#.
 * 8 ........#.
 * 9 #########.
 *
 * Sand is produced one unit at a time, and the next unit of sand is not produced until the previous unit of sand comes to rest. A unit of sand is large enough to fill one tile of air in your scan.
 *
 * A unit of sand always falls down one step if possible. If the tile immediately below is blocked (by rock or sand), the unit of sand attempts to instead move diagonally one step down and to the left. If that tile is blocked, the unit of sand attempts to instead move diagonally one step down and to the right. Sand keeps moving as long as it is able to do so, at each step trying to move down, then down-left, then down-right. If all three possible destinations are blocked, the unit of sand comes to rest and no longer moves, at which point the next unit of sand is created back at the source.
 *
 * So, drawing sand that has come to rest as o, the first unit of sand simply falls straight down and then stops:
 *
 * ......+...
 * ..........
 * ..........
 * ..........
 * ....#...##
 * ....#...#.
 * ..###...#.
 * ........#.
 * ......o.#.
 * #########.
 *
 * The second unit of sand then falls straight down, lands on the first one, and then comes to rest to its left:
 *
 * ......+...
 * ..........
 * ..........
 * ..........
 * ....#...##
 * ....#...#.
 * ..###...#.
 * ........#.
 * .....oo.#.
 * #########.
 *
 * After a total of five units of sand have come to rest, they form this pattern:
 *
 * ......+...
 * ..........
 * ..........
 * ..........
 * ....#...##
 * ....#...#.
 * ..###...#.
 * ......o.#.
 * ....oooo#.
 * #########.
 *
 * After a total of 22 units of sand:
 *
 * ......+...
 * ..........
 * ......o...
 * .....ooo..
 * ....#ooo##
 * ....#ooo#.
 * ..###ooo#.
 * ....oooo#.
 * ...ooooo#.
 * #########.
 *
 * Finally, only two more units of sand can possibly come to rest:
 *
 * ......+...
 * ..........
 * ......o...
 * .....ooo..
 * ....#ooo##
 * ...o#ooo#.
 * ..###ooo#.
 * ....oooo#.
 * .o.ooooo#.
 * #########.
 *
 * Once all 24 units of sand shown above have come to rest, all further sand flows out the bottom, falling into the endless void. Just for fun, the path any new sand takes before falling forever is shown here with ~:
 *
 * .......+...
 * .......~...
 * ......~o...
 * .....~ooo..
 * ....~#ooo##
 * ...~o#ooo#.
 * ..~###ooo#.
 * ..~..oooo#.
 * .~o.ooooo#.
 * ~#########.
 * ~..........
 * ~..........
 * ~..........
 *
 * Using your scan, simulate the falling sand. How many units of sand come to rest before sand starts flowing into the abyss below?
 *
 */
private fun part1(lines: List<String>): Int {
    val rocks = lines.toRocks()
    val filled = rocks.toMutableSet()
    val abyss = filled.maxOf { it.first } + 1
    var c = 0

    while (true) {
        var cur = 0 to 500

        while (true) {
            if (cur.first >= abyss) {
                return c
            }

            if (cur.first + 1 to cur.second !in filled) {
                cur = cur.first + 1 to cur.second
                continue
            }

            if (cur.first + 1 to cur.second - 1 !in filled) {
                cur = cur.first + 1 to cur.second - 1
                continue
            }

            if (cur.first + 1 to cur.second + 1 !in filled) {
                cur = cur.first + 1 to cur.second + 1
                continue
            }

            filled += cur
            ++c
            break
        }
    }
}

/**
 * --- Part Two ---
 *
 * You realize you misread the scan. There isn't an endless void at the bottom of the scan - there's floor, and you're standing on it!
 *
 * You don't have time to scan the floor, so assume the floor is an infinite horizontal line with a y coordinate equal to two plus the highest y coordinate of any point in your scan.
 *
 * In the example above, the highest y coordinate of any point is 9, and so the floor is at y=11. (This is as if your scan contained one extra rock path like -infinity,11 -> infinity,11.) With the added floor, the example above now looks like this:
 *
 *         ...........+........
 *         ....................
 *         ....................
 *         ....................
 *         .........#...##.....
 *         .........#...#......
 *         .......###...#......
 *         .............#......
 *         .............#......
 *         .....#########......
 *         ....................
 * <-- etc #################### etc -->
 *
 * To find somewhere safe to stand, you'll need to simulate falling sand until a unit of sand comes to rest at 500,0, blocking the source entirely and stopping the flow of sand into the cave. In the example above, the situation finally looks like this after 93 units of sand come to rest:
 *
 * ............o............
 * ...........ooo...........
 * ..........ooooo..........
 * .........ooooooo.........
 * ........oo#ooo##o........
 * .......ooo#ooo#ooo.......
 * ......oo###ooo#oooo......
 * .....oooo.oooo#ooooo.....
 * ....oooooooooo#oooooo....
 * ...ooo#########ooooooo...
 * ..ooooo.......ooooooooo..
 * #########################
 *
 * Using your scan, simulate the falling sand until the source of the sand becomes blocked. How many units of sand come to rest?
 *
 */
private fun part2(lines: List<String>): Int {
    val rocks = lines.toRocks()
    val filled = rocks.toMutableSet()
    val abyss = filled.maxOf { it.first } + 1
    var c = 0

    val start = 0 to 500
    while (start !in filled) {
        var cur = start

        while (true) {
            if (cur.first >= abyss) {
                break
            }

            if (cur.first + 1 to cur.second !in filled) {
                cur = cur.first + 1 to cur.second
                continue
            }

            if (cur.first + 1 to cur.second - 1 !in filled) {
                cur = cur.first + 1 to cur.second - 1
                continue
            }

            if (cur.first + 1 to cur.second + 1 !in filled) {
                cur = cur.first + 1 to cur.second + 1
                continue
            }

            break
        }

        filled += cur
        ++c
    }

    return c
}

fun main() {

    val testInput = readInput("aoc_2022/Day14_test")
    val input = readInput("aoc_2022/Day14")

    check(part1(testInput) == 24)
    part1(input).println()

    check(part2(testInput) == 93)
    part2(input).println()

}
