package aoc_2023

import println
import readInput

private const val MIN = 200000000000000.0
private const val MAX = 400000000000000.0

private typealias Point = Triple<Long, Long, Long>
private typealias Velocity = Triple<Long, Long, Long>
private typealias Hailstone = Pair<Point, Velocity>

private fun List<String>.parse(): List<Hailstone> = map { it.split(" @ ") }
    .map { (left, right) ->
        val (px, py, pz) = left.split(",")
            .map { it.trim() }
            .map { it.toLong() }
        val (vx, vy, vz) = right.split(",")
            .map { it.trim() }
            .map { it.toLong() }

        Triple(px, py, pz) to Triple(vx, vy, vz)
    }

private fun Hailstone.intersects(other: Hailstone, min: Double, max: Double): Boolean {
    val (p1, v1) = this
    val (p2, v2) = other
    val (x1, y1) = p1
    val (x2, y2) = p2
    val dx = x2 - x1
    val dy = y2 - y1
    val (vx1, vy1) = v1
    val (vx2, vy2) = v2

    val delta = vx2 * vy1 - vx1 * vy2
    val delta1 = vx2 * dy - vy2 * dx
    val delta2 = vx1 * dy - vy1 * dx
    if (delta == 0L) {
        return delta1 == 0L && delta2 == 0L
    }

    val t1 = delta1.toDouble() / delta
    val t2 = delta2.toDouble() / delta
    if (t1 <= 0 || t2 <= 0) {
        return false
    }

    val x11 = x1 + vx1 * t1
    val y11 = y1 + vy1 * t1

    return x11 in min..max && y11 in min..max
}

private fun List<Hailstone>.countFutureIntersections(min: Double, max: Double): Int {
    var count = 0

    for ((i, hi) in withIndex()) {
        for (hj in subList(i + 1, size)) {
            if (hi.intersects(hj, min, max)) {
                ++count
            }
        }
    }

    return count
}

/**
 * --- Day 24: Never Tell Me The Odds ---
 *
 * It seems like something is going wrong with the snow-making process. Instead of forming snow, the water that's been absorbed into the air seems to be forming hail!
 *
 * Maybe there's something you can do to break up the hailstones?
 *
 * Due to strong, probably-magical winds, the hailstones are all flying through the air in perfectly linear trajectories. You make a note of each hailstone's position and velocity (your puzzle input). For example:
 *
 * 19, 13, 30 @ -2,  1, -2
 * 18, 19, 22 @ -1, -1, -2
 * 20, 25, 34 @ -2, -2, -4
 * 12, 31, 28 @ -1, -2, -1
 * 20, 19, 15 @  1, -5, -3
 *
 * Each line of text corresponds to the position and velocity of a single hailstone. The positions indicate where the hailstones are right now (at time 0). The velocities are constant and indicate exactly how far each hailstone will move in one nanosecond.
 *
 * Each line of text uses the format px py pz @ vx vy vz. For instance, the hailstone specified by 20, 19, 15 @ 1, -5, -3 has initial X position 20, Y position 19, Z position 15, X velocity 1, Y velocity -5, and Z velocity -3. After one nanosecond, the hailstone would be at 21, 14, 12.
 *
 * Perhaps you won't have to do anything. How likely are the hailstones to collide with each other and smash into tiny ice crystals?
 *
 * To estimate this, consider only the X and Y axes; ignore the Z axis. Looking forward in time, how many of the hailstones' paths will intersect within a test area? (The hailstones themselves don't have to collide, just test for intersections between the paths they will trace.)
 *
 * In this example, look for intersections that happen with an X and Y position each at least 7 and at most 27; in your actual data, you'll need to check a much larger test area. Comparing all pairs of hailstones' future paths produces the following results:
 *
 * Hailstone A: 19, 13, 30 @ -2, 1, -2
 * Hailstone B: 18, 19, 22 @ -1, -1, -2
 * Hailstones' paths will cross inside the test area (at x=14.333, y=15.333).
 *
 * Hailstone A: 19, 13, 30 @ -2, 1, -2
 * Hailstone B: 20, 25, 34 @ -2, -2, -4
 * Hailstones' paths will cross inside the test area (at x=11.667, y=16.667).
 *
 * Hailstone A: 19, 13, 30 @ -2, 1, -2
 * Hailstone B: 12, 31, 28 @ -1, -2, -1
 * Hailstones' paths will cross outside the test area (at x=6.2, y=19.4).
 *
 * Hailstone A: 19, 13, 30 @ -2, 1, -2
 * Hailstone B: 20, 19, 15 @ 1, -5, -3
 * Hailstones' paths crossed in the past for hailstone A.
 *
 * Hailstone A: 18, 19, 22 @ -1, -1, -2
 * Hailstone B: 20, 25, 34 @ -2, -2, -4
 * Hailstones' paths are parallel; they never intersect.
 *
 * Hailstone A: 18, 19, 22 @ -1, -1, -2
 * Hailstone B: 12, 31, 28 @ -1, -2, -1
 * Hailstones' paths will cross outside the test area (at x=-6, y=-5).
 *
 * Hailstone A: 18, 19, 22 @ -1, -1, -2
 * Hailstone B: 20, 19, 15 @ 1, -5, -3
 * Hailstones' paths crossed in the past for both hailstones.
 *
 * Hailstone A: 20, 25, 34 @ -2, -2, -4
 * Hailstone B: 12, 31, 28 @ -1, -2, -1
 * Hailstones' paths will cross outside the test area (at x=-2, y=3).
 *
 * Hailstone A: 20, 25, 34 @ -2, -2, -4
 * Hailstone B: 20, 19, 15 @ 1, -5, -3
 * Hailstones' paths crossed in the past for hailstone B.
 *
 * Hailstone A: 12, 31, 28 @ -1, -2, -1
 * Hailstone B: 20, 19, 15 @ 1, -5, -3
 * Hailstones' paths crossed in the past for both hailstones.
 *
 * So, in this example, 2 hailstones' future paths cross inside the boundaries of the test area.
 *
 * However, you'll need to search a much larger test area if you want to see if any hailstones might collide. Look for intersections that happen with an X and Y position each at least 200000000000000 and at most 400000000000000. Disregard the Z axis entirely.
 *
 * Considering only the X and Y axes, check all pairs of hailstones' future paths for intersections. How many of these intersections occur within the test area?
 *
 */
private fun part1(lines: List<String>, min: Double = MIN, max: Double = MAX): Int {
    val hailstones = lines.parse()
    val intersections = hailstones.countFutureIntersections(min, max)

    return intersections
}

private fun List<Hailstone>.calculateRockCoordsSum(): Long {
    val times = (0..2).firstNotNullOf { k ->
        indices.firstNotNullOfOrNull { i ->
            val (pi, vi) = this[i]
            val pil = pi.toList()
            val vil = vi.toList()

            indices
                .map { j ->
                    val (pj, vj) = this[j]
                    val pjl = pj.toList()
                    val vjl = vj.toList()
                    val dpk = pjl[k] - pil[k]
                    val dvk = vil[k] - vjl[k]

                    if (dvk == 0L) {
                        if (dpk == 0L) 0L else -1L
                    } else {
                        if (dpk % dvk == 0L) dpk / dvk else -1L
                    }
                }
                .takeIf { times -> times.all { it >= 0 } }
        }
    }
    val (i, j) = times.withIndex()
        .filter { it.value > 0 }
        .map { it.index }

    // pik(t) = pik(0) + vik * t
    fun coordAtTime(i: Int, k: Int, t: Long): Long {
        val (p, v) = this[i]
        val pik = p.toList()[k]
        val vik = v.toList()[k]

        return pik + vik * t
    }

    val ti = times[i]
    val tj = times[j]
    val dt = tj - ti

    return (0..2).sumOf { k ->
        val pik = coordAtTime(i, k, ti)
        val pjk = coordAtTime(j, k, tj)
        val dpk = pjk - pik

        // pik(t) = p0k + dpk / dt * ti
        pik - dpk / dt * ti
    }
}

/**
 * --- Part Two ---
 *
 * Upon further analysis, it doesn't seem like any hailstones will naturally collide. It's up to you to fix that!
 *
 * You find a rock on the ground nearby. While it seems extremely unlikely, if you throw it just right, you should be able to hit every hailstone in a single throw!
 *
 * You can use the probably-magical winds to reach any integer position you like and to propel the rock at any integer velocity. Now including the Z axis in your calculations, if you throw the rock at time 0, where do you need to be so that the rock perfectly collides with every hailstone? Due to probably-magical inertia, the rock won't slow down or change direction when it collides with a hailstone.
 *
 * In the example above, you can achieve this by moving to position 24, 13, 10 and throwing the rock at velocity -3, 1, 2. If you do this, you will hit every hailstone as follows:
 *
 * Hailstone: 19, 13, 30 @ -2, 1, -2
 * Collision time: 5
 * Collision position: 9, 18, 20
 *
 * Hailstone: 18, 19, 22 @ -1, -1, -2
 * Collision time: 3
 * Collision position: 15, 16, 16
 *
 * Hailstone: 20, 25, 34 @ -2, -2, -4
 * Collision time: 4
 * Collision position: 12, 17, 18
 *
 * Hailstone: 12, 31, 28 @ -1, -2, -1
 * Collision time: 6
 * Collision position: 6, 19, 22
 *
 * Hailstone: 20, 19, 15 @ 1, -5, -3
 * Collision time: 1
 * Collision position: 21, 14, 12
 *
 * Above, each hailstone is identified by its initial position and its velocity. Then, the time and position of that hailstone's collision with your rock are given.
 *
 * After 1 nanosecond, the rock has exactly the same position as one of the hailstones, obliterating it into ice dust! Another hailstone is smashed to bits two nanoseconds after that. After a total of 6 nanoseconds, all of the hailstones have been destroyed.
 *
 * So, at time 0, the rock needs to be at X position 24, Y position 13, and Z position 10. Adding these three coordinates together produces 47. (Don't add any coordinates from the rock's velocity.)
 *
 * Determine the exact position and velocity the rock needs to have at time 0 so that it perfectly collides with every hailstone. What do you get if you add up the X, Y, and Z coordinates of that initial position?
 *
 */
private fun part2(lines: List<String>): Long {
    val hailstones = lines.parse()
    val sum = hailstones.calculateRockCoordsSum()

    return sum
}

fun main() {

    val testInput = readInput("aoc_2023/Day24_test")
    val input = readInput("aoc_2023/Day24")

    check(part1(testInput, 7.0, 27.0) == 2)
    part1(input).println()

    check(part2(testInput) == 47L)
    part2(input).println()

}
