package island

import kotlin.random.Random

class Wolf : Carnivore(50.0, 30, 3, 8.0) {
    companion object {
        private val PREY_CHANCES = intArrayOf(0, 0, 0, 0, 0, 10, 15, 60, 80, 60, 70, 15, 10, 40, 0, 0)
    }

    override fun tryEat(target: Entity, rand: Random): Boolean {
        val chance = when (target) {
            is Horse -> PREY_CHANCES[5]
            is Deer -> PREY_CHANCES[6]
            is Rabbit -> PREY_CHANCES[7]
            is Mouse -> PREY_CHANCES[8]
            is Goat -> PREY_CHANCES[9]
            is Sheep -> PREY_CHANCES[10]
            is Boar -> PREY_CHANCES[11]
            is Buffalo -> PREY_CHANCES[12]
            is Duck -> PREY_CHANCES[13]
            else -> -1
        }
        return chance > 0 && rand.nextInt(100) < chance
    }

    override fun toString() = "🐺"
}