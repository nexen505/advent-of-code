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
    .let { line -> if (removeBlank) line.filter { it.isNotBlank() } else line }

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

private tailrec infix fun Long.gcd(b: Long): Long = if (b == 0L) this else b gcd (this % b)
infix fun Long.lcm(b: Long): Long = this * b / (this gcd b)

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    companion object {
        fun parse(c: Char): Direction = when (c) {
            'U' -> UP
            'R' -> RIGHT
            'D' -> DOWN
            'L' -> LEFT
            else -> error("Unknown direction $c")
        }
    }
}
