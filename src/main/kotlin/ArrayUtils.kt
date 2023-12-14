fun Array<CharArray>.transposed(): Array<CharArray> {
    val rows = this.size
    val cols = this[0].size

    return Array(cols) { j -> CharArray(rows) { i -> this[i][j] } }
}

fun List<String>.toCharArray2(): Array<CharArray> = map { it.toCharArray() }.toTypedArray()
