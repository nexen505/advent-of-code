package aoc_2023

import println
import readInput
import java.util.EnumMap
import java.util.LinkedList

private const val ACCEPTED = "A"
private const val REJECTED = "R"

private typealias Rating = EnumMap<Category, Long>
private typealias Workflow = Pair<String, List<(Rating) -> String?>>
private typealias Workflows = Map<String, List<(Rating) -> String?>>

private enum class Category {
    X, M, A, S
}

private fun String.parseWorkflow(): Workflow {
    val delim = indexOf('{')
    val name = substring(0, delim)
    val rules: List<(Rating) -> String?> = substring(delim)
        .trim('{', '}')
        .split(',')
        .map { s ->
            val ruleDelim = s.indexOf(':')
            if (ruleDelim < 0) {
                return@map { s }
            }

            val (criterion, dest) = s.split(':')

            val gtIdx = criterion.indexOf('>')
            if (gtIdx >= 0) {
                val category = Category.valueOf(criterion.substring(0, gtIdx).uppercase())
                val value = criterion.substring(gtIdx + 1).toLong()

                return@map { if (it[category] != null && it[category]!! > value) dest else null }
            } else {
                val ltIdx = criterion.indexOf('<')
                val category = Category.valueOf(criterion.substring(0, ltIdx).uppercase())
                val value = criterion.substring(ltIdx + 1).toLong()

                return@map { if (it[category] != null && it[category]!! < value) dest else null }
            }
        }

    return name to rules
}

private fun String.parseRating(): Rating = trim('{', '}')
    .split(',')
    .map { it.split('=') }
    .associateTo(EnumMap(Category::class.java)) { (c, v) ->
        val category = Category.valueOf(c.uppercase())
        val value = v.toLong()

        category to value
    }

private fun List<String>.parse(): Pair<Workflows, List<Rating>> {
    val delimiterIdx = indexOf("")
    val workflowStrings = subList(0, delimiterIdx)
    val ratingStrings = subList(delimiterIdx + 1, size)
    val workflows = workflowStrings
        .filterNot { it.isBlank() }
        .map { it.parseWorkflow() }
        .associateBy({ it.first }, { it.second })
    val ratings = ratingStrings
        .filterNot { it.isBlank() }
        .map { it.parseRating() }

    return workflows to ratings
}

private fun Workflows.accepts(rating: Rating): Boolean {
    var start: String? = "in"

    while (start != null && start !in setOf(ACCEPTED, REJECTED)) {
        val workflow = this[start]!!

        start = workflow.firstNotNullOfOrNull { it.invoke(rating) }
    }

    return start == ACCEPTED
}

/**
 * --- Day 19: Aplenty ---
 *
 * The Elves of Gear Island are thankful for your help and send you on your way. They even have a hang glider that someone stole from Desert Island; since you're already going that direction, it would help them a lot if you would use it to get down there and return it to them.
 *
 * As you reach the bottom of the relentless avalanche of machine parts, you discover that they're already forming a formidable heap. Don't worry, though - a group of Elves is already here organizing the parts, and they have a system.
 *
 * To start, each part is rated in each of four categories:
 *
 *     x: Extremely cool looking
 *     m: Musical (it makes a noise when you hit it)
 *     a: Aerodynamic
 *     s: Shiny
 *
 * Then, each part is sent through a series of workflows that will ultimately accept or reject the part. Each workflow has a name and contains a list of rules; each rule specifies a condition and where to send the part if the condition is true. The first rule that matches the part being considered is applied immediately, and the part moves on to the destination described by the rule. (The last rule in each workflow has no condition and always applies if reached.)
 *
 * Consider the workflow ex{x>10:one,m<20:two,a>30:R,A}. This workflow is named ex and contains four rules. If workflow ex were considering a specific part, it would perform the following steps in order:
 *
 *     Rule "x>10:one": If the part's x is more than 10, send the part to the workflow named one.
 *     Rule "m<20:two": Otherwise, if the part's m is less than 20, send the part to the workflow named two.
 *     Rule "a>30:R": Otherwise, if the part's a is more than 30, the part is immediately rejected (R).
 *     Rule "A": Otherwise, because no other rules matched the part, the part is immediately accepted (A).
 *
 * If a part is sent to another workflow, it immediately switches to the start of that workflow instead and never returns. If a part is accepted (sent to A) or rejected (sent to R), the part immediately stops any further processing.
 *
 * The system works, but it's not keeping up with the torrent of weird metal shapes. The Elves ask if you can help sort a few parts and give you the list of workflows and some part ratings (your puzzle input). For example:
 *
 * px{a<2006:qkq,m>2090:A,rfg}
 * pv{a>1716:R,A}
 * lnx{m>1548:A,A}
 * rfg{s<537:gd,x>2440:R,A}
 * qs{s>3448:A,lnx}
 * qkq{x<1416:A,crn}
 * crn{x>2662:A,R}
 * in{s<1351:px,qqz}
 * qqz{s>2770:qs,m<1801:hdj,R}
 * gd{a>3333:R,R}
 * hdj{m>838:A,pv}
 *
 * {x=787,m=2655,a=1222,s=2876}
 * {x=1679,m=44,a=2067,s=496}
 * {x=2036,m=264,a=79,s=2244}
 * {x=2461,m=1339,a=466,s=291}
 * {x=2127,m=1623,a=2188,s=1013}
 *
 * The workflows are listed first, followed by a blank line, then the ratings of the parts the Elves would like you to sort. All parts begin in the workflow named in. In this example, the five listed parts go through the following workflows:
 *
 *     {x=787,m=2655,a=1222,s=2876}: in -> qqz -> qs -> lnx -> A
 *     {x=1679,m=44,a=2067,s=496}: in -> px -> rfg -> gd -> R
 *     {x=2036,m=264,a=79,s=2244}: in -> qqz -> hdj -> pv -> A
 *     {x=2461,m=1339,a=466,s=291}: in -> px -> qkq -> crn -> R
 *     {x=2127,m=1623,a=2188,s=1013}: in -> px -> rfg -> A
 *
 * Ultimately, three parts are accepted. Adding up the x, m, a, and s rating for each of the accepted parts gives 7540 for the part with x=787, 4623 for the part with x=2036, and 6951 for the part with x=2127. Adding all of the ratings for all of the accepted parts gives the sum total of 19114.
 *
 * Sort through all of the parts you've been given; what do you get if you add together all of the rating numbers for all of the parts that ultimately get accepted?
 *
 *
 */
private fun part1(lines: List<String>): Long {
    val (workflows, ratings) = lines.parse()

    return ratings
        .filter { workflows.accepts(it) }
        .sumOf { it.values.sum() }
}

/**
 * --- Part Two ---
 *
 * Even with your help, the sorting process still isn't fast enough.
 *
 * One of the Elves comes up with a new plan: rather than sort parts individually through all of these workflows, maybe you can figure out in advance which combinations of ratings will be accepted or rejected.
 *
 * Each of the four ratings (x, m, a, s) can have an integer value ranging from a minimum of 1 to a maximum of 4000. Of all possible distinct combinations of ratings, your job is to figure out which ones will be accepted.
 *
 * In the above example, there are 167409079868000 distinct combinations of ratings that will be accepted.
 *
 * Consider only your list of workflows; the list of part ratings that the Elves wanted you to sort is no longer relevant. How many distinct combinations of ratings will be accepted by the Elves' workflows?
 */
private fun part2(lines: List<String>): Long {
    val minValue = 1L
    val maxValue = 4000L
    val workflowRanges = lines.parseWorkflowRanges(minValue..maxValue)
    val queue = LinkedList(
        listOf(
            EnumMap(
                mapOf(
                    Category.X to minValue..maxValue,
                    Category.M to minValue..maxValue,
                    Category.A to minValue..maxValue,
                    Category.S to minValue..maxValue
                )
            ) to "in"
        )
    )

    while (queue.any { it.second != ACCEPTED }) {
        val nonAccepted = queue.first { it.second != ACCEPTED }
        queue.remove(nonAccepted)
        val (oldRanges, oldDestination) = nonAccepted
        if (oldDestination == REJECTED) {
            continue
        }

        val rules = workflowRanges[oldDestination]!!
        val currentRanges = EnumMap(oldRanges)
        for ((range, destination) in rules) {
            val newRanges = EnumMap(currentRanges)
            if (range == null) {
                queue.add(newRanges to destination)
                break
            }

            val (category, workflowRange) = range
            if (category !in oldRanges) {
                continue
            }

            val currentRange = currentRanges[category]!!
            val curl = currentRange.first
            val curr = currentRange.last
            val wrl = workflowRange.first
            val wrr = workflowRange.last
            val newl = maxOf(curl, wrl)
            val newr = minOf(curr, wrr)
            if (newl > newr) {
                continue
            }

            val newRange = newl..newr
            newRanges[category] = newRange
            queue.add(newRanges to destination)
            if (currentRange == newRange) {
                break
            }

            if (newl in currentRange && curl < newl) {
                currentRanges[category] = curl..<newl
            } else if (newr in currentRange && curr > newr) {
                currentRanges[category] = newr + 1..curr
            }
        }
    }

    val sum = queue
        .map { it.first }
        .sumOf { m ->
            m.values.fold(1L) { res, r ->
                res * (r.last - r.first + 1)
            }
        }
    return sum
}

private typealias WorkflowRange = Pair<Pair<Category, LongRange>?, String>
private typealias WorkflowRanges = List<WorkflowRange>

private fun List<String>.parseWorkflowRanges(range: LongRange): Map<String, WorkflowRanges> {
    val delimiterIdx = indexOf("")
    val workflowStrings = subList(0, delimiterIdx)

    return workflowStrings
        .filterNot { it.isBlank() }
        .map { it.parseWorkflowRange(range) }
        .associateBy({ it.first }, { it.second })
}

private fun String.parseWorkflowRange(range: LongRange): Pair<String, WorkflowRanges> {
    val delim = indexOf('{')
    val name = substring(0, delim)
    val ranges: WorkflowRanges = substring(delim)
        .trim('{', '}')
        .split(',')
        .map { s ->
            val ruleDelim = s.indexOf(':')
            if (ruleDelim < 0) {
                return@map null to s
            }

            val (criterion, dest) = s.split(':')

            val gtIdx = criterion.indexOf('>')
            return@map if (gtIdx >= 0) {
                val category = Category.valueOf(criterion.substring(0, gtIdx).uppercase())
                val value = criterion.substring(gtIdx + 1).toLong()

                (category to value + 1..range.last) to dest
            } else {
                val ltIdx = criterion.indexOf('<')
                val category = Category.valueOf(criterion.substring(0, ltIdx).uppercase())
                val value = criterion.substring(ltIdx + 1).toLong()

                (category to range.first..<value) to dest
            }
        }

    return name to ranges
}

fun main() {

    val testInput = readInput("aoc_2023/Day19_test", false)
    val input = readInput("aoc_2023/Day19", false)

    check(part1(testInput) == 19114L)
    part1(input).println()

    check(part2(testInput) == 167409079868000L)
    part2(input).println()

}
