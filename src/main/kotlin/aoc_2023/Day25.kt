package aoc_2023

import println
import readInput

private typealias Edge = Pair<Int, Int>

private fun Set<Edge>.containsEdge(e: Edge): Boolean {
    val (u, v) = e

    return contains(u to v) || contains(v to u)
}

private class Graph(val edges: Set<Edge>, private val vertices: Int) {

    private companion object {
        var time = 1
    }

    private val adjacency = Array(vertices) { i ->
        edges
            .filter { it.toList().contains(i) }
            .flatMap { it.toList() - i }
            .distinct()
            .sorted()
            .toIntArray()
    }

    fun findBridges(excludedEdges: Set<Edge>): Set<Edge> {
        val bridges = mutableSetOf<Edge>()

        time = 1
        val disc = IntArray(vertices)
        val low = IntArray(vertices)

        for (v in 0..<vertices) {
            if (disc[v] == 0) {
                dfsBridges(bridges, excludedEdges, disc, low, v)
            }
        }

        return bridges
    }

    private fun dfsBridges(
        bridges: MutableSet<Edge>,
        excludedEdges: Set<Edge>,
        disc: IntArray,
        low: IntArray,
        curr: Int,
        prev: Int = -1
    ) {
        disc[curr] = time
        low[curr] = time
        ++time

        for (next in adjacency[curr]) {
            if (next == prev || excludedEdges.containsEdge(curr to next)) {
                continue
            }

            if (disc[next] == 0) {
                dfsBridges(bridges, excludedEdges, disc, low, next, curr)

                low[curr] = minOf(low[curr], low[next])
                if (low[next] > disc[curr]) {
                    bridges.add(curr to next)
                }
            } else {
                low[curr] = minOf(low[curr], disc[next])
            }
        }
    }

    fun getTwoComponentsSizesMultiplication(excludedEdges: Set<Edge>): Int {
        val visited = BooleanArray(vertices)

        for (v in 0..<vertices) {
            if (!visited[v]) {
                dfs(excludedEdges, visited, v)

                val component1Size = visited.count { it }
                val component2Size = vertices - component1Size

                return component1Size * component2Size
            }
        }

        error("Two components cannot be found")
    }

    private fun dfs(excludedEdges: Set<Edge>, visited: BooleanArray, v: Int) {
        visited[v] = true

        for (to in adjacency[v]) {
            if (!visited[to] && !excludedEdges.containsEdge(v to to)) {
                dfs(excludedEdges, visited, to)
            }
        }
    }

}

private fun String.parse(): Pair<String, List<String>> {
    val (vertex, right) = split(": ")
    val adjacents = right
        .splitToSequence(" ")
        .map { it.trim() }
        .toList()

    return vertex to adjacents
}

private fun List<String>.parse(): Graph {
    val vertexIndices = asSequence()
        .map { it.parse() }
        .flatMap { (v, adj) -> adj.asSequence() + v }
        .distinct()
        .withIndex()
        .associate { (idx, v) -> v to idx }
    val edges = mutableSetOf<Edge>()

    for (line in this) {
        val (vertex, adjacents) = line.parse()
        val vi = vertexIndices[vertex]!!

        for (adjacent in adjacents) {
            val adji = vertexIndices[adjacent]!!
            val edge = vi to adji

            if (!edges.containsEdge(edge)) {
                edges += edge
            }
        }
    }

    return Graph(edges, vertexIndices.size)
}

/**
 * --- Day 25: Snowverload ---
 *
 * Still somehow without snow, you go to the last place you haven't checked: the center of Snow Island, directly below the waterfall.
 *
 * Here, someone has clearly been trying to fix the problem. Scattered everywhere are hundreds of weather machines, almanacs, communication modules, hoof prints, machine parts, mirrors, lenses, and so on.
 *
 * Somehow, everything has been wired together into a massive snow-producing apparatus, but nothing seems to be running. You check a tiny screen on one of the communication modules: Error 2023. It doesn't say what Error 2023 means, but it does have the phone number for a support line printed on it.
 *
 * "Hi, you've reached Weather Machines And So On, Inc. How can I help you?" You explain the situation.
 *
 * "Error 2023, you say? Why, that's a power overload error, of course! It means you have too many components plugged in. Try unplugging some components and--" You explain that there are hundreds of components here and you're in a bit of a hurry.
 *
 * "Well, let's see how bad it is; do you see a big red reset button somewhere? It should be on its own module. If you push it, it probably won't fix anything, but it'll report how overloaded things are." After a minute or two, you find the reset button; it's so big that it takes two hands just to get enough leverage to push it. Its screen then displays:
 *
 * SYSTEM OVERLOAD!
 *
 * Connected components would require
 * power equal to at least 100 stars!
 *
 * "Wait, how many components did you say are plugged in? With that much equipment, you could produce snow for an entire--" You disconnect the call.
 *
 * You have nowhere near that many stars - you need to find a way to disconnect at least half of the equipment here, but it's already Christmas! You only have time to disconnect three wires.
 *
 * Fortunately, someone left a wiring diagram (your puzzle input) that shows how the components are connected. For example:
 *
 * jqt: rhn xhk nvd
 * rsh: frs pzl lsr
 * xhk: hfx
 * cmg: qnr nvd lhk bvb
 * rhn: xhk bvb hfx
 * bvb: xhk hfx
 * pzl: lsr hfx nvd
 * qnr: nvd
 * ntq: jqt hfx bvb xhk
 * nvd: lhk
 * lsr: lhk
 * rzs: qnr cmg lsr rsh
 * frs: qnr lhk lsr
 *
 * Each line shows the name of a component, a colon, and then a list of other components to which that component is connected. Connections aren't directional; abc: xyz and xyz: abc both represent the same configuration. Each connection between two components is represented only once, so some components might only ever appear on the left or right side of a colon.
 *
 * In this example, if you disconnect the wire between hfx/pzl, the wire between bvb/cmg, and the wire between nvd/jqt, you will divide the components into two separate, disconnected groups:
 *
 *     9 components: cmg, frs, lhk, lsr, nvd, pzl, qnr, rsh, and rzs.
 *     6 components: bvb, hfx, jqt, ntq, rhn, and xhk.
 *
 * Multiplying the sizes of these groups together produces 54.
 *
 * Find the three wires you need to disconnect in order to divide the components into two separate groups. What do you get if you multiply the sizes of these two groups together?
 *
 */
private fun part1(lines: List<String>): Int {
    val graph = lines.parse()
    val edges = graph.edges.toList()
    val edgesCount = edges.size

    for (i in 0..<edgesCount) {
        val ei = edges[i]

        for (j in i + 1..<edgesCount) {
            val ej = edges[j]
            val excludedEdges = setOf(ei, ej)
            val bridges = graph.findBridges(excludedEdges)

            if (bridges.size == 1) {
                val bridgesToAvoid = excludedEdges + bridges.single()

                return graph.getTwoComponentsSizesMultiplication(bridgesToAvoid)
            }
        }
    }

    error("Two components cannot be found")
}

fun main() {

    val testInput = readInput("aoc_2023/Day25_test")
    val input = readInput("aoc_2023/Day25")

    check(part1(testInput) == 54)
    part1(input).println()

}
