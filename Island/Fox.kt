package island

import kotlin.random.Random

class Fox : Carnivore(8.0, 30, 2, 2.0) {
    companion object {
        private val PREY_CHANCES = intArrayOf(0, 0, 0, 0, 0, 0, 0, 70, 90, 0, 0, 0, 0, 60, 40, 0)
    }

    override fun tryEat(target: Entity, rand: Random): Boolean {
        val chance = when (target) {
            is Rabbit -> PREY_CHANCES[7]
            is Mouse -> PREY_CHANCES[8]
            is Duck -> PREY_CHANCES[13]
            is Caterpillar -> PREY_CHANCES[14]
            else -> -1
        }
        return chance > 0 && rand.nextInt(100) < chance
    }

    override fun toString() = "🦊"
}