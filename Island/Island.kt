package island

import kotlin.random.Random

class Island(val width: Int, val height: Int) {
    private val grid: Array<Array<Cell>> = Array(height) { y ->
        Array(width) { x -> Cell(x, y, this) }
    }

    fun populate() {
        for (row in grid) {
            for (cell in row) {
                with(Random) {
                    if (nextDouble() < 0.05) cell.addEntity(Wolf())
                    if (nextDouble() < 0.05) cell.addEntity(Boa())
                    if (nextDouble() < 0.08) cell.addEntity(Fox())
                    if (nextDouble() < 0.1) cell.addEntity(Bear())
                    if (nextDouble() < 0.05) cell.addEntity(Eagle())
                    if (nextDouble() < 0.1) cell.addEntity(Horse())
                    if (nextDouble() < 0.1) cell.addEntity(Deer())
                    if (nextDouble() < 0.15) cell.addEntity(Rabbit())
                    if (nextDouble() < 0.2) cell.addEntity(Mouse())
                    if (nextDouble() < 0.1) cell.addEntity(Goat())
                    if (nextDouble() < 0.1) cell.addEntity(Sheep())
                    if (nextDouble() < 0.08) cell.addEntity(Boar())
                    if (nextDouble() < 0.05) cell.addEntity(Buffalo())
                    if (nextDouble() < 0.15) cell.addEntity(Duck())
                    if (nextDouble() < 0.2) cell.addEntity(Caterpillar())
                    if (nextDouble() < 0.25) cell.addEntity(Plant())
                }
            }
        }
    }

    fun simulateDay() {
        grid.forEach { row -> row.forEach { it.simulate() } }
    }

    fun printStats() {
        val counts = mutableMapOf<String, Int>()
        grid.flatten().flatMap { it.entities }.forEach { entity ->
            counts[entity.toString()] = counts.getOrDefault(entity.toString(), 0) + 1
        }
        println(counts.entries.joinToString(" | ") { "${it.key}: ${it.value}" })
    }

    fun getCell(x: Int, y: Int): Cell = grid[y][x]
}