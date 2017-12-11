package com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator

import gnu.trove.list.array.TIntArrayList
import jnisat.JPicoSat

/**
 * @return null if not satisfiable
 */
fun isSolvable(clauses: List<TIntArrayList>): Boolean {
    val sat = initSat(clauses)
    return sat.solve()
}

/**
 * @return null if not satisfiable
 * @param clauses a list of ints. Clauses are separated by 0
 */
fun solve(clauses: List<TIntArrayList>, varCount: Int): List<Int>? {
    val sat = initSat(clauses)
    val res = sat.solve()
    if (!res) {
        return null
    }
    val solution = ArrayList<Int>()
    for (variable in 1..varCount) {
        val value = sat.getValue(variable)
        when {
            value < 0 -> solution.add(-variable)
            value > 0 -> solution.add(variable)
            else -> solution.add(-variable) // variable may be any
        }
    }
    return solution
}

private fun initSat(clausesList: List<TIntArrayList>): JPicoSat {
    val sat = JPicoSat()
    for (clauses in clausesList) {
        var startIndex = 0
        for (i in 0 until clauses.size()) {
            if (clauses[i] == 0) {
                sat.addClause(*clauses.subList(startIndex, i).toArray())
                startIndex = i + 1
            }
        }
    }
    return sat
}