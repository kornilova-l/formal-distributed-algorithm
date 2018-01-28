package com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.collections

import com.github.kornilova_l.algorithm_synthesis.grid2D.independent_set.IndependentSetTile.Companion.getTilesFile
import com.github.kornilova_l.algorithm_synthesis.grid2D.independent_set.IndependentSetTile.Companion.parseTiles
import com.github.kornilova_l.algorithm_synthesis.grid2D.independent_set.IndependentSetTileGenerator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class SimpleGraphWithTilesTest {
    private val tiles32 = IndependentSetTileGenerator(3, 2, 1).tiles
    private val tiles23 = IndependentSetTileGenerator(2, 3, 1).tiles
    private val tiles67 = parseTiles(getTilesFile(6, 7, 3, File("independent_set_tiles"))!!)
    private val tiles58 = parseTiles(getTilesFile(5, 8, 3, File("independent_set_tiles"))!!)

    @Test
    fun getGraph() {
        var tileGraph = SimpleGraphWithTiles.createInstance(tiles32, tiles23)
        assertEquals(7, tileGraph.size)

        assertEquals(15, tileGraph.edgeCount)

        tileGraph = SimpleGraphWithTiles.createInstance(tiles67, tiles58)
        assertEquals(2079, tileGraph.size)
    }

    @Test
    fun exportAndImport() {
        val graph = SimpleGraphWithTiles.createInstance(tiles32, tiles23)
        val file = File("temp.txt")
        graph.export(file)

        val parsedGraph = SimpleGraph(file)
        assertEquals(graph.graph, parsedGraph.graph)

        file.delete()
    }
}