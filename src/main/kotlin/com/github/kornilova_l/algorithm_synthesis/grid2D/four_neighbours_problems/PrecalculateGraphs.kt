package com.github.kornilova_l.algorithm_synthesis.grid2D.four_neighbours_problems

import com.github.kornilova_l.algorithm_synthesis.grid2D.four_neighbours_problems.graphs.FourNeighboursGraphWithTiles
import com.github.kornilova_l.algorithm_synthesis.grid2D.independent_set.IndependentSetTile
import com.github.kornilova_l.algorithm_synthesis.grid2D.independent_set.createDir
import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.BinaryTile.Companion.getM
import com.github.kornilova_l.algorithm_synthesis.grid2D.tiles.BinaryTile.Companion.getN
import com.github.kornilova_l.util.Logger
import java.io.File
import java.nio.file.Paths

private val tilesDir = File("independent_set_tiles")
private val graphsDir = Paths.get(tilesDir.toString(), "four_neighbours_directed_graphs").toFile()
private val logger = Logger(File("precalculate_is_tiles.log"))

/**
 * Precalculate graphs and export them to files.
 * So [FourNeighboursProblemSolver] instances can be created and used.
 */
fun main(args: Array<String>) {
    if (!tilesDir.exists()) {
        logger.error("Tiles directory does not exist. ${tilesDir.absolutePath}")
        return
    }
    createDir(graphsDir, logger)
    val files = tilesDir.listFiles()
    for (i in 0 until files.size) {
        val file = files[i]
        if (IndependentSetTile.independentSetTilesFilePattern.matcher(file.name).matches()) {
            val n = getN(file)
            val m = getM(file)
            if (n < 2 || m < 2) {
                continue
            }
            logger.info("Process tiles from file: ${file.name}")
            val tileSet = IndependentSetTile.parseTiles(file)
            val graph = FourNeighboursGraphWithTiles.createInstance(tileSet)
            logger.info("Start exporting tiles...")
            graph.exportTiles(graphsDir)
            println("Start exporting graph...")
            graph.export(graphsDir)
        }
    }
}