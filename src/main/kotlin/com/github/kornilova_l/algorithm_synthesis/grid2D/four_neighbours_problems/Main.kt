package com.github.kornilova_l.algorithm_synthesis.grid2D.four_neighbours_problems

import com.github.kornilova_l.algorithm_synthesis.grid2D.five_neighbours_problems.printResult
import com.github.kornilova_l.algorithm_synthesis.grid2D.four_neighbours_problems.problem.FourNeighboursProblem


fun main(args: Array<String>) {
    val problem = FourNeighboursProblem("?1?0")
    val labelingFunction = FourNeighboursProblemSolver().getLabelingFunction(problem) ?: return
    printResult(labelingFunction.first, labelingFunction.second)
}