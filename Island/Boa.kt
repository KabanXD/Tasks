package island

import kotlin.random.Random

class Boa : Carnivore(15.0, 30, 1, 3.0) {
    companion object {
        private val PREY_CHANCES = intArrayOf(0, 0, 15, 0, 0, 0, 0, 20, 40, 0, 0, 0, 0, 10, 0, 0)
    }

    override fun tryEat(target: Entity, rand: Random): Boolean {
        val chance = when (target) {
            is Fox -> PREY_CHANCES[2]
            is Rabbit -> PREY_CHANCES[7]
            is Mouse -> PREY_CHANCES[8]
            is Duck -> PREY_CHANCES[13]
            else -> -1
        }
        return chance > 0 && rand.nextInt(100) < chance
    }

    override fun toString() = "🐍"
}