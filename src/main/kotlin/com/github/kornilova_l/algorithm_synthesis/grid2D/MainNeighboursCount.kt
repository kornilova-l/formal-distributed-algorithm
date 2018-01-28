package com.github.kornilova_l.algorithm_synthesis.grid2D

import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.rule.Problem
import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.rule.VertexRule
import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.rule.getRulePermutations
import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.tryToFindSolutionForEachProblem
import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.visualization.GridDrawer
import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.visualization.RowDrawer
import java.util.*
import kotlin.collections.ArrayList


/**
 * List<List<Int>>
 * each element of list contains combination of numbers
 * each number tell how many neighbours will be included.
 * For example, if list contains 0, 1, 4
 * it means that a node may have 0, 1 or 4 included neighbours.
 * So XN, N, NESW, X, ... combinations are valid (it is not specified if center is included)
 * and NS, NSW, ... combinations are invalid
 */
val neighboursCombinationsList = (1..31).map { i ->
    val list = ArrayList<Int>(5)
    (0..4).forEach { pos ->
        if (i.and(1.shl(pos)) != 0) {
            list.add(pos)
        }
    }
    list
}.sortedWith(kotlin.Comparator { c1, c2 ->
            when {
                c1.size < c2.size -> -1
                c1.size > c2.size -> 1
                else -> { // sizes of combinations are equal
                    for (i in 0 until c1.size) {
                        if (c1[i] < c2[i]) {
                            return@Comparator -1
                        } else if (c1[i] > c2[i]) {
                            return@Comparator 1
                        }
                    }
                    0
                }
            }
        })


fun main(args: Array<String>) {
//    calc1DScheme()
    calc2DScheme()
}

fun calc2DScheme() {
    /* maps problem to combination for included center and excluded center */
    val problemToCombinations = HashMap<Problem, Pair<List<Int>, List<Int>>>()
    val problems = ArrayList<Problem>()
    for (combinationForIncludedCenter in neighboursCombinationsList) {
        for (combinationForExcludedCenter in neighboursCombinationsList) {
            val problem = createProblem(combinationForIncludedCenter, combinationForExcludedCenter)
            problemToCombinations[problem] = Pair(combinationForIncludedCenter, combinationForExcludedCenter)
            problems.add(problem)
        }
    }
    val solvableProblems = tryToFindSolutionForEachProblem(problems)
    val solvableCombinations = getSolvableCombinations(solvableProblems, problemToCombinations)
    for (solvableCombination in solvableCombinations) {
        println(solvableCombination)
    }
    val grid = createGrid(solvableCombinations)
    val labels = neighboursCombinationsList.map { it.toString() }
    GridDrawer(labels, labels.reversed(), grid).outputImage()
}

fun createGrid(solvableCombinations: Set<Pair<List<Int>, List<Int>>>): Array<BooleanArray> {
    val size = neighboursCombinationsList.size
    val grid = Array(size, { BooleanArray(size, { false }) })
    for (pair in solvableCombinations) {
        val combinationForIncludedCenter = pair.first
        val combinationForExcludedCenter = pair.second
        grid[getId(combinationForIncludedCenter)][size - 1 - getId(combinationForExcludedCenter)] = true
    }
    return grid
}

fun getId(combinationForExcludedCenter: List<Int>): Int {
    for (i in 0 until neighboursCombinationsList.size) {
        if (combinationForExcludedCenter == neighboursCombinationsList[i]) {
            return i
        }
    }
    throw IllegalArgumentException("Combination was not found in the list")
}

/**
 * [neighboursCombinationsList] contains different combinations of how
 * many neighbours are included.
 * A problem is created for each combination.
 * For example, if combination is 0, 3 create a problem in which any
 * cell has 0 or 3 neighbours (it does not matter if cell itself is included or not).
 * It is easy to see that if combination contain 0 or 4 then problem has trivial solution
 * (all cells excluded or all cells included)
 *
 * Then we try to find solution for each problem and visualize result
 * using [Drawer1D.draw(List<Point>)]
 */
fun calc1DScheme() {
    val problemToCombination = HashMap<Problem, List<Int>>()
    val problems = ArrayList<Problem>()
    for (neighboursCombination in neighboursCombinationsList) {
        val problem = createProblem(neighboursCombination)
        problemToCombination[problem] = neighboursCombination
        problems.add(problem)
    }
    val solvableProblems = tryToFindSolutionForEachProblem(problems)
    val solvableCombinations = getSolvableCombinations(solvableProblems, problemToCombination)
    val rowDrawer = createDrawer(neighboursCombinationsList, solvableCombinations)
    rowDrawer.outputImage()
}

private fun createDrawer(neighboursCombinationsList: List<List<Int>>, solvableCombinations: Set<List<Int>>): RowDrawer {
    val row = BooleanArray(neighboursCombinationsList.size, { false })
    val labels = ArrayList<String>()
    neighboursCombinationsList.forEachIndexed { i, combination ->
        val listToString = combination.toString()
        val label = listToString.substring(1, listToString.length - 1) // remove '[' and ']'
        labels.add(label)
        row[i] = solvableCombinations.contains(combination)
    }
    return RowDrawer(labels, row)
}

/**
 * Method [tryToFindSolutionForEachProblem] returns list of [Problem]
 * and we need to get combinations which were used to create these problems.
 */
private fun <T> getSolvableCombinations(solvableProblems: Set<Problem>,
                                        problemToCombination: HashMap<Problem, T>): Set<T> {
    val solvableCombinations = HashSet<T>()
    for (solvableProblem in solvableProblems) {
        solvableCombinations.add(problemToCombination[solvableProblem]!!)
    }
    return solvableCombinations
}

/**
 * @param combination1 combination of neighbours for included cell
 * @param combination2 combination of neighbours for excluded cell (if not specified it is the same as combination1)
 */
private fun createProblem(combination1: ArrayList<Int>, combination2: ArrayList<Int> = combination1): Problem {
    val rules = HashSet<VertexRule>()
    for (includedNeighboursCount in combination1) {
        rules.addAll(getRulePermutations(includedNeighboursCount, true))
    }
    for (includedNeighboursCount in combination2) {
        rules.addAll(getRulePermutations(includedNeighboursCount, false))
    }
    return Problem(rules)
}