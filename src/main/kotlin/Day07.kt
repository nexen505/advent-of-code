fun createComparator(cards: List<Char>): Comparator<String> = Comparator { o1, o2 ->
    val o1num = o1.toList().map { cards.indexOf(it) }
    val o2num = o2.toList().map { cards.indexOf(it) }

    for ((index, v1) in o1num.withIndex()) {
        val v2 = o2num[index]
        val cmp = v1.compareTo(v2)

        if (cmp != 0) {
            return@Comparator cmp
        }
    }

    return@Comparator 0
}

const val jack = 'J'
val normalCards = listOf('A', 'K', 'Q', jack, 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
val normalCardsComparator: Comparator<String> = createComparator(normalCards)

const val joker = jack
val jokerCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', joker).reversed()
val jokerCardsComparator: Comparator<String> = createComparator(jokerCards)

private enum class Type {
    HIGH, PAIR, TWO_PAIR, THREE, FULL_HOUSE, FOUR, FIVE;

    companion object {

        fun defineNormally(card: String): Type {
            val chars = card
                .toList()
                .groupingBy { it }
                .eachCount()

            return defineNormally(chars)
        }

        private fun defineNormally(chars: Map<Char, Int>): Type {
            if (chars.containsValue(5)) {
                return FIVE
            }

            if (chars.containsValue(4)) {
                return FOUR
            }

            if (chars.containsValue(3)) {
                if (chars.containsValue(2)) {
                    return FULL_HOUSE
                }

                return THREE
            }

            val pairs = chars.values.count { it == 2 }
            return when (pairs) {
                2 -> TWO_PAIR
                1 -> PAIR
                else -> HIGH
            }
        }

        fun defineJoking(card: String): Type {
            val chars = card
                .toList()
                .groupingBy { it }
                .eachCount()
                .toMutableMap()
            val jokerCount = chars.remove(joker)
            val normalType = defineNormally(chars)
            if (jokerCount == null) {
                return normalType
            }

            return when (normalType) {
                HIGH -> when (jokerCount) {
                    1 -> PAIR
                    2 -> THREE
                    3 -> FOUR
                    else -> FIVE
                }
                PAIR -> when (jokerCount) {
                    1 -> THREE
                    2 -> FOUR
                    else -> FIVE
                }
                TWO_PAIR -> when (jokerCount) {
                    1 -> FULL_HOUSE
                    else -> normalType
                }
                THREE -> when (jokerCount) {
                    1 -> FOUR
                    else -> FIVE
                }
                FOUR -> FIVE
                else -> normalType
            }
        }

    }
}

fun main() {

    fun List<String>.toPairs(typeFn: (String) -> Type): List<Pair<String, Pair<Type, Long>>> = map {
        val (card, bid) = it.split(" ")
        val cardTrimmed = card.trim()

        cardTrimmed to (typeFn(cardTrimmed) to bid.trim().toLong())
    }

    fun List<Pair<String, Pair<Type, Long>>>.calculateTotalWinnings(comparator: Comparator<String>): Long {
        val rankedCards = sortedWith(
            compareBy<Pair<String, Pair<Type, Long>>> { it.second.first }
                .thenBy(comparator) { it.first }
        )

        return rankedCards
            .withIndex()
            .sumOf { (idx, pair) -> (idx + 1) * pair.second.second }
    }

    fun part1(lines: List<String>): Long {
        val cards = lines.toPairs(Type::defineNormally)

        return cards.calculateTotalWinnings(normalCardsComparator)
    }

    fun part2(lines: List<String>): Long {
        val cards = lines.toPairs(Type::defineJoking)

        return cards.calculateTotalWinnings(jokerCardsComparator)
    }

    val testInput1 = readInput("Day07_test")
    val testInput2 = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput1) == 6440L)
    part1(input).println()

    check(part2(testInput2) == 5905L)
    part2(input).println()
}
