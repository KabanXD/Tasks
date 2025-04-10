package island

import kotlin.random.Random

class Plant : Entity() {
    override fun act(cell: Cell) {
        if (Random.nextDouble() < 0.1 && cell.entities.count { it is Plant } < 200) {
            cell.addEntity(Plant())
        }
    }

    override fun toString() = "🌿"
}