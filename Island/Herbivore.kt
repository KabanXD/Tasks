package island

abstract class Herbivore(weight: Double, maxPerCell: Int, speed: Int, foodNeeded: Double) :
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
        cell.entities.find { it is Plant }?.let {
            cell.entities.remove(it)
            currentHunger = foodNeeded
        }
    }
}