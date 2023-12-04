private const val DELIMITER = '.'
private const val GEAR = '*'

fun main() {

    fun List<String>.toChar2Array(): Array<CharArray> {
        val n = this.size
        val m = this.first().length
        val chars = Array(n) { CharArray(m) }
        for (i in 0 until n) {
            val line = this[i]

            for (j in 0 until m) {
                chars[i][j] = line[j]
            }
        }

        return chars
    }

    fun part1(lines: List<String>): Int {
        if (lines.isEmpty()) {
            return 0
        }

        var sum = 0L

        val chars = lines.toChar2Array()
        val n = chars.size
        val m = chars.first().size

        for (i in 0 until n) {
            var j = 0

            while (j < m) {
                var c = chars[i][j]
                if (!c.isDigit()) {
                    ++j
                    continue
                }

                val j0 = j
                var num = 0L
                while (c.isDigit()) {
                    num = num * 10 + c.digitToInt()
                    ++j
                    if (j >= m) {
                        break
                    }

                    c = chars[i][j]
                }

                var isPart = false
                var i1 = i - 1
                while (!isPart && i1 <= i + 1) {
                    var j1 = j0 - 1
                    while (!isPart && j1 <= j) {
                        if (i1 >= 0 && j1 >= 0 && i1 < n && j1 < m) {
                            val adjc = chars[i1][j1]

                            if (!adjc.isDigit() && adjc != DELIMITER) {
                                isPart = true
                            }
                        }

                        j1++
                    }

                    i1++
                }

                if (isPart) {
                    sum += num
                }
            }
        }

        return sum.toInt()
    }

    fun part2(lines: List<String>): Int {
        if (lines.isEmpty()) {
            return 0
        }

        val chars = lines.toChar2Array()
        val n = chars.size
        val m = chars.first().size

        val gr = Array(n) { LongArray(m) { 1 } }
        val gc = Array(n) { IntArray(m) }
        for (i in 0 until n) {
            var j = 0

            while (j < m) {
                var c = chars[i][j]
                if (!c.isDigit()) {
                    ++j
                    continue
                }

                val j0 = j
                var num = 0L
                while (Character.isDigit(c)) {
                    num = num * 10 + c.digitToInt()
                    ++j
                    if (j >= m) {
                        break
                    }

                    c = chars[i][j]
                }

                for (i1 in i - 1..i + 1) {
                    for (j1 in j0 - 1..j) {
                        if (i1 >= 0 && j1 >= 0 && i1 < n && j1 < m && chars[i1][j1] == GEAR) {
                            ++gc[i1][j1]
                            gr[i1][j1] *= num
                        }
                    }
                }
            }
        }

        var sum = 0L
        for (i in gc.indices) {
            for (j in gc[i].indices) {
                if (gc[i][j] == 2) {
                    sum += gr[i][j]
                }
            }
        }

        return sum.toInt()
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
