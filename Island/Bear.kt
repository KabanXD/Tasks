package island

import kotlin.random.Random

class Bear : Carnivore(500.0, 5, 2, 80.0) {
    companion object {
        private val PREY_CHANCES = intArrayOf(0, 80, 0, 0, 0, 40, 80, 80, 90, 70, 70, 50, 20, 10, 0, 0)
    }

    override fun tryEat(target: Entity, rand: Random): Boolean {
        val chance = when (target) {
            is Boa -> PREY_CHANCES[1]
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

    override fun toString() = "🐻"
}