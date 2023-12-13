import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, removeBlank: Boolean = true) = Thread
    .currentThread()
    .contextClassLoader
    .getResource("$name.txt")!!
    .readText()
    .lines()
    .let { if (removeBlank) it.filter { it.isNotBlank() } else it }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Array<CharArray>.transposed(): Array<CharArray> {
    val rows = this.size
    val cols = this[0].size

    return Array(cols) { j -> CharArray(rows) { i -> this[i][j] } }
}
