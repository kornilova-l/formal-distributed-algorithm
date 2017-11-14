package com.github.kornilova_l.algorithm_synthesis.tiles

import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.TileGenerator
import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.TileGraph
import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.TileSet
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class TileGraphTest {
    private val tiles32 = TileGenerator(3, 2, 1).tileSet
    private val tiles23 = TileGenerator(2, 3, 1).tileSet
    private val tiles67 = TileSet(File("generated_tiles/6-7-3.txt"))
    private val tiles58 = TileSet(File("generated_tiles/5-8-3.txt"))

    @Test
    fun getGraph() {
        var tileGraph = TileGraph(tiles32, tiles23)
        assertEquals(7, tileGraph.size)

        assertEquals(15, tileGraph.edgeCount)

        tileGraph = TileGraph(tiles67, tiles58)
        assertEquals(2079, tileGraph.size)
    }

}