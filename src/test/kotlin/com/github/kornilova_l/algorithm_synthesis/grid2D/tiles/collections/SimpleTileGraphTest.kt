package com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.collections

import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.IndependentSetTileGenerator
import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.parseTiles
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class SimpleTileGraphTest {
    private val tiles32 = IndependentSetTileGenerator(3, 2, 1).tiles
    private val tiles23 = IndependentSetTileGenerator(2, 3, 1).tiles
    private val tiles67 = parseTiles(File("generated_tiles/6-7-3.txt"))
    private val tiles58 = parseTiles(File("generated_tiles/5-8-3.txt"))

    @Test
    fun getGraph() {
        var tileGraph = SimpleTileGraph(tiles32, tiles23)
        assertEquals(7, tileGraph.size)

        assertEquals(15, tileGraph.edgeCount)

        tileGraph = SimpleTileGraph(tiles67, tiles58)
        assertEquals(2079, tileGraph.size)
    }

}