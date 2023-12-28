fun List<String>.toCharArray2(): Array<CharArray> = map { it.toCharArray() }.toTypedArray()

fun Array<BooleanArray>.size2(): Pair<Int, Int> = size to this[0].size
fun Array<CharArray>.size2(): Pair<Int, Int> = size to this[0].size
fun Array<IntArray>.size2(): Pair<Int, Int> = size to this[0].size

fun Array<CharArray>.deepCopyOf(): Array<CharArray> = Array(size) { this[it].copyOf() }

fun Array<CharArray>.transposed(): Array<CharArray> {
    val rows = this.size
    val cols = this[0].size

    return Array(cols) { j -> CharArray(rows) { i -> this[i][j] } }
}

fun Array<CharArray>.rotateRight(): Array<CharArray> {
    val transposed = this.transposed()

    transposed.reverseRows()

    return transposed
}

private fun Array<CharArray>.reverseRows() {
    for (row in this) {
        val size = row.size

        for (j in 0..<size / 2) {
            val k = size - j - 1
            val tmp = row[j]
            row[j] = row[k]
            row[k] = tmp
        }
    }
}
