import kotlin.math.abs
import kotlin.math.hypot

// trapezoid formula https://en.wikipedia.org/wiki/Shoelace_formula
fun Iterable<Pair<Long, Long>>.calculateArea(): Double {
    var area = 0.0
    for ((prev, cur) in cycle()) {
        area += (prev.second + cur.second) * (prev.first - cur.first) / 2.0
    }

    return abs(area)
}

fun Iterable<Pair<Long, Long>>.calculatePerimeter(): Double {
    var length = 0.0
    for ((prev, cur) in cycle()) {
        length += hypot(cur.first.toDouble() - prev.first, cur.second.toDouble() - prev.second)
    }

    return length
}

// https://en.wikipedia.org/wiki/Pick%27s_theorem
fun Iterable<Pair<Long, Long>>.calculateInterior(): Double = calculateArea() - calculatePerimeter() / 2 + 1

private fun Iterable<Pair<Long, Long>>.cycle(): Sequence<List<Pair<Long, Long>>> =
    asSequence().windowed(2) + sequenceOf(listOf(last(), first()))

fun Iterable<Long>.mul(): Long = fold(1L) { a, b -> a * b }

fun Iterable<Long>.lcm(): Long = fold(1L) { res, v -> res lcm v }
fun Sequence<Long>.lcm(): Long = fold(1L) { res, v -> res lcm v }

fun <T> List<T>.equalsIgnoreOrder(other: List<T>) = this.size == other.size && this.toSet() == other.toSet()
