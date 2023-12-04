private const val RED = "red"
private const val GREEN = "green"
private const val BLUE = "blue"

fun main() {

    fun part1(lines: List<String>): Int {
        var sum = 0
        var idx = 1

        for (line in lines) {
            val (_, setsString) = line.split(": ")
            val sets = setsString.split("; ")
            val isOk = sets.all { set ->
                val colorStrings = set.split(", ")

                colorStrings.all { colorString ->
                    val (quantity, color) = colorString.split(" ")

                    when (color) {
                        RED -> quantity.toInt() <= 12
                        GREEN -> quantity.toInt() <= 13
                        BLUE -> quantity.toInt() <= 14
                        else -> error("Unexpected value: $color")
                    }
                }
            }

            if (isOk) {
                sum += idx
            }

            ++idx
        }

        return sum
    }

    fun part2(lines: List<String>): Int = lines.sumOf { line ->
        val counts = mutableMapOf(
            RED to 0,
            GREEN to 0,
            BLUE to 0
        )

        val (_, setsString) = line.split(": ")
        val sets = setsString.split("; ")

        for (set in sets) {
            val colorStrings = set.split(", ")

            for (colorString in colorStrings) {
                val (quantity, color) = colorString.split(" ")

                counts.computeIfPresent(color) { _, v -> maxOf(quantity.toInt(), v) }
            }
        }

        if (counts.isEmpty()) 0 else counts.values.fold(1) { a, b -> a * b }.toInt()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
