package island

import kotlin.random.Random

abstract class Carnivore(weight: Double, maxPerCell: Int, speed: Int, foodNeeded: Double) :
    Animal(weight, maxPerCell, speed, foodNeeded) {
    override fun act(cell: Cell) {
        currentHunger -= 0.1
        if (currentHunger <= 0) {
            cell.entities.remove(this)
            return
        }
        eat(cell)
        move(cell)
    }

    override fun eat(cell: Cell) {
        val prey = cell.entities.toList()
        val rand = Random
        for (target in prey) {
            if (target != this && tryEat(target, rand)) {
                cell.entities.remove(target)
                currentHunger = foodNeeded
                break
            }
        }
    }

    protected abstract fun tryEat(target: Entity, rand: Random): Boolean
}