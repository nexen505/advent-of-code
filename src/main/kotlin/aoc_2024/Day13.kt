package aoc_2024

import println
import readInput

/**
 * --- Day 13: Claw Contraption ---
 *
 * Next up: the lobby of a resort on a tropical island. The Historians take a moment to admire the hexagonal floor tiles before spreading out.
 *
 * Fortunately, it looks like the resort has a new arcade! Maybe you can win some prizes from the claw machines?
 *
 * The claw machines here are a little unusual. Instead of a joystick or directional buttons to control the claw, these machines have two buttons labeled A and B. Worse, you can't just put in a token and play; it costs 3 tokens to push the A button and 1 token to push the B button.
 *
 * With a little experimentation, you figure out that each machine's buttons are configured to move the claw a specific amount to the right (along the X axis) and a specific amount forward (along the Y axis) each time that button is pressed.
 *
 * Each machine contains one prize; to win the prize, the claw must be positioned exactly above the prize on both the X and Y axes.
 *
 * You wonder: what is the smallest number of tokens you would have to spend to win as many prizes as possible? You assemble a list of every machine's button behavior and prize location (your puzzle input). For example:
 *
 * Button A: X+94, Y+34
 * Button B: X+22, Y+67
 * Prize: X=8400, Y=5400
 *
 * Button A: X+26, Y+66
 * Button B: X+67, Y+21
 * Prize: X=12748, Y=12176
 *
 * Button A: X+17, Y+86
 * Button B: X+84, Y+37
 * Prize: X=7870, Y=6450
 *
 * Button A: X+69, Y+23
 * Button B: X+27, Y+71
 * Prize: X=18641, Y=10279
 *
 * This list describes the button configuration and prize location of four different claw machines.
 *
 * For now, consider just the first claw machine in the list:
 *
 *     Pushing the machine's A button would move the claw 94 units along the X axis and 34 units along the Y axis.
 *     Pushing the B button would move the claw 22 units along the X axis and 67 units along the Y axis.
 *     The prize is located at X=8400, Y=5400; this means that from the claw's initial position, it would need to move exactly 8400 units along the X axis and exactly 5400 units along the Y axis to be perfectly aligned with the prize in this machine.
 *
 * The cheapest way to win the prize is by pushing the A button 80 times and the B button 40 times. This would line up the claw along the X axis (because 80*94 + 40*22 = 8400) and along the Y axis (because 80*34 + 40*67 = 5400). Doing this would cost 80*3 tokens for the A presses and 40*1 for the B presses, a total of 280 tokens.
 *
 * For the second and fourth claw machines, there is no combination of A and B presses that will ever win a prize.
 *
 * For the third claw machine, the cheapest way to win the prize is by pushing the A button 38 times and the B button 86 times. Doing this would cost a total of 200 tokens.
 *
 * So, the most prizes you could possibly win is two; the minimum tokens you would have to spend to win all (two) prizes is 480.
 *
 * You estimate that each button would need to be pressed no more than 100 times to win a prize. How else would someone be expected to play?
 *
 * Figure out how to win as many prizes as possible. What is the fewest tokens you would have to spend to win all possible prizes?
 */
private fun part1(lines: List<String>): Long =
    lines
        .parse()
        .map { it.solve() }
        .sumOf { (x, y) ->
            if (x in 0..100 && y in 0..100) {
                x * 3 + y
            } else {
                0L
            }
        }

private fun List<String>.parse(): Sequence<Triple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>>> =
    asSequence()
        .chunked(3) { block ->
            block
                .map { line ->
                    line.substringAfter(": ")
                        .split(", ")
                        .map {
                            it
                                .substringAfter('+')
                                .substringAfter('=')
                                .toLong()
                        }
                        .let { (a, b) -> a to b }
                }
                .let { (a, b, c) -> Triple(a, b, c) }
        }

private fun Triple<Pair<Long, Long>, Pair<Long, Long>, Pair<Long, Long>>.solve(): Pair<Long, Long> {
    val (l1, l2, l3) = this
    val (a, c) = l1
    val (b, d) = l2
    val (e, f) = l3
    val delta = a * d - b * c
    require(delta != 0L)
    val x = (e * d - b * f) / delta.toDouble()
    val y = (e - a * x) / b

    return if (x % 1 == 0.0 && y % 1 == 0.0) x.toLong() to y.toLong() else 0L to 0L
}

/**
 * --- Part Two ---
 *
 * As you go to win the first prize, you discover that the claw is nowhere near where you expected it would be. Due to a unit conversion error in your measurements, the position of every prize is actually 10000000000000 higher on both the X and Y axis!
 *
 * Add 10000000000000 to the X and Y position of every prize. After making this change, the example above would now look like this:
 *
 * Button A: X+94, Y+34
 * Button B: X+22, Y+67
 * Prize: X=10000000008400, Y=10000000005400
 *
 * Button A: X+26, Y+66
 * Button B: X+67, Y+21
 * Prize: X=10000000012748, Y=10000000012176
 *
 * Button A: X+17, Y+86
 * Button B: X+84, Y+37
 * Prize: X=10000000007870, Y=10000000006450
 *
 * Button A: X+69, Y+23
 * Button B: X+27, Y+71
 * Prize: X=10000000018641, Y=10000000010279
 *
 * Now, it is only possible to win a prize on the second and fourth claw machines. Unfortunately, it will take many more than 100 presses to do so.
 *
 * Using the corrected prize coordinates, figure out how to win as many prizes as possible. What is the fewest tokens you would have to spend to win all possible prizes?
 */
private fun part2(lines: List<String>): Long = lines
    .parse()
    .map { (l1, l2, l3) ->
        var (a, b) = l3
        a += 10000000000000
        b += 10000000000000

        Triple(l1, l2, a to b)
    }
    .map { it.solve() }
    .sumOf { (x, y) -> x * 3 + y }

/**
 * --- Part Two ---
 *
 * As you go to win the first prize, you discover that the claw is nowhere near where you expected it would be. Due to a unit conversion error in your measurements, the position of every prize is actually 10000000000000 higher on both the X and Y axis!
 *
 * Add 10000000000000 to the X and Y position of every prize. After making this change, the example above would now look like this:
 *
 * Button A: X+94, Y+34
 * Button B: X+22, Y+67
 * Prize: X=10000000008400, Y=10000000005400
 *
 * Button A: X+26, Y+66
 * Button B: X+67, Y+21
 * Prize: X=10000000012748, Y=10000000012176
 *
 * Button A: X+17, Y+86
 * Button B: X+84, Y+37
 * Prize: X=10000000007870, Y=10000000006450
 *
 * Button A: X+69, Y+23
 * Button B: X+27, Y+71
 * Prize: X=10000000018641, Y=10000000010279
 *
 * Now, it is only possible to win a prize on the second and fourth claw machines. Unfortunately, it will take many more than 100 presses to do so.
 *
 * Using the corrected prize coordinates, figure out how to win as many prizes as possible. What is the fewest tokens you would have to spend to win all possible prizes?
 */
fun main() {
    val testInput = readInput("aoc_2024/Day13_test")
    val input = readInput("aoc_2024/Day13")

    check(part1(testInput) == 480L)
    part1(input).println()

    part2(input).println()
}
