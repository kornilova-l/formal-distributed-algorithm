package com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator

import jnisat.JPicoSat

private val sat = JPicoSat()

/**
 * @return null if not satisfiable
 */
fun isSolvable(clauses: Set<Set<Int>>): Boolean {
    initSat(clauses)
    return sat.solve()
}

/**
 * @return null if not satisfiable
 */
fun solve(clauses: Set<Set<Int>>, varCount: Int): List<Int>? {
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
            else -> throw RuntimeException("Variable cannot be found in sat")
        }
    }
    return solution
}

private fun initSat(clauses: Set<Set<Int>>): JPicoSat {
    sat.reset()
    for (clause in clauses) {
        sat.addClause(*clause.toIntArray())
    }
    return sat
}