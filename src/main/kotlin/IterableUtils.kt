import kotlin.math.hypot

// trapezoid formula https://en.wikipedia.org/wiki/Shoelace_formula
fun Iterable<Pair<Long, Long>>.calculateArea(): Long {
    var area = 0L
    for ((prev, cur) in cycle()) {
        area += (prev.second + cur.second) * (prev.first - cur.first) / 2
    }

    return area
}

private fun Iterable<Pair<Long, Long>>.cycle() =
    asSequence().windowed(2) + sequenceOf(listOf(last(), first()))

fun Iterable<Pair<Long, Long>>.calculateLength(): Long {
    var length = 0L
    for ((prev, cur) in asSequence().windowed(2) + sequenceOf(listOf(last(), first()))) {
        length += hypot(cur.first.toDouble() - prev.first, cur.second.toDouble() - prev.second).toLong()
    }

    return length
}
