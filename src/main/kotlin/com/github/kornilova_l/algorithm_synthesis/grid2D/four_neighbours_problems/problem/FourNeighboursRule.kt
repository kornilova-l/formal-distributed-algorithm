package com.github.kornilova_l.algorithm_synthesis.grid2D.four_neighbours_problems.problem

import com.github.kornilova_l.algorithm_synthesis.grid2D.vertex_set_generator.problem.VertexRule
import java.util.*


val fourNeighboursNonTrivialRules = (1 until 15).map { FourNeighboursRule(it) }.toSet()

class FourNeighboursRule : VertexRule {
    /* top left, top right, bottom right, bottom left */
    override val array = BooleanArray(4)

    override val id: Int

    constructor(id: Int) {
        if (id !in 0 until 16) {
            throw IllegalArgumentException("Id must be in [0, 15]. Id: $id")
        }
        this.id = id
        setArrayValues(id, array)
    }

    constructor(array: BooleanArray) {
        if (array.size != 4) {
            throw IllegalArgumentException("Size of array must be 4. Array: $array")
        }
        System.arraycopy(array, 0, this.array, 0, this.array.size)
        id = calcId(array)
    }

    constructor(string: String) {
        val parts = string.split(" ").filter { it != "" }
        val array = BooleanArray(4)
        for (part in parts) {
            val position = FourPositions.positionLetters.getKey(part)!!
            array[FourPositions.positionIndexes[position]!!] = true
        }
        id = calcId(array)
    }

    override fun rotate(rotationsCount: Int): FourNeighboursRule {
        val rotatedArray = BooleanArray(4)
        for (i in 0 until 4) {
            rotatedArray[(i + rotationsCount) % 4] = array[i]
        }
        return FourNeighboursRule(rotatedArray)
    }

    override fun toHumanReadableSting(): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until array.size) {
            if (array[i]) {
                stringBuilder.append(FourPositions.positionIndexes.getKey(i)).append(" ")
            }
        }
        return "[" + stringBuilder.toString().removeSuffix(" ") + "]"
    }

    fun isIncluded(position: FOUR_POSITION): Boolean = array[FourPositions.positionIndexes[position]!!]

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FourNeighboursRule) {
            return false
        }
        return id == other.id
    }
}