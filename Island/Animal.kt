package island

import kotlin.random.Random

abstract class Animal(
    val weight: Double,
    val maxPerCell: Int,
    val speed: Int,
    val foodNeeded: Double
) : Entity() {
    var currentHunger: Double = foodNeeded

    fun move(cell: Cell) {
        val rand = Random
        val dx = rand.nextInt(speed + 1) * if (rand.nextBoolean()) 1 else -1
        val dy = rand.nextInt(speed + 1) * if (rand.nextBoolean()) 1 else -1
        val newX = (cell.x + dx).coerceIn(0, cell.island.width - 1)
        val newY = (cell.y + dy).coerceIn(0, cell.island.height - 1)
        val newCell = cell.island.getCell(newX, newY)

        if (canMoveTo(newCell)) {
            cell.entities.remove(this)
            newCell.addEntity(this)
        }
    }

    protected fun canMoveTo(cell: Cell): Boolean {
        val sameTypeCount = cell.entities.count { it::class == this::class }
        return sameTypeCount < maxPerCell
    }

    abstract fun eat(cell: Cell)
}