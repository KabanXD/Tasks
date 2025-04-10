import kotlin.random.Random
import kotlin.concurrent.thread

fun main() {
    val simulator = WorldSimulator()
    simulator.startSimulation()
}

class WorldSimulator {
    private val island = Island(10, 10)

    fun startSimulation() {
        println("Starting Island Simulation...")
        island.populate()
        var day = 1
        while (true) {
            println("\n--- Day $day ---")
            island.simulateDay()
            island.printStats()
            Thread.sleep(1000) // Задержка 1 секунда
            day++
        }
    }
}

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

class Cell(val x: Int, val y: Int, val island: Island) {
    val entities: MutableList<Entity> = mutableListOf()

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun simulate() {
        entities.toList().forEach { it.act(this) } // toList() для избежания ConcurrentModification
    }

    override fun toString() = "($x,$y)"
}

abstract class Entity {
    abstract fun act(cell: Cell)
    abstract override fun toString(): String
}

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

class Rabbit : Herbivore(2.0, 150, 2, 0.45) {
    override fun toString() = "🐇"
}

class Deer : Herbivore(300.0, 20, 4, 50.0) {
    override fun toString() = "🦌"
}

class Plant : Entity() {
    override fun act(cell: Cell) {
        if (Random.nextDouble() < 0.1 && cell.entities.count { it is Plant } < 200) {
            cell.addEntity(Plant())
        }
    }

    override fun toString() = "🌿"
}

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

class Eagle : Carnivore(6.0, 20, 3, 1.0) {
    companion object {
        private val PREY_CHANCES = intArrayOf(0, 0, 10, 0, 0, 0, 0, 90, 90, 0, 0, 0, 0, 80, 0, 0)
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

    override fun toString() = "🦅"
}

class Horse : Herbivore(400.0, 20, 4, 60.0) {
    override fun toString() = "🐎"
}

class Mouse : Herbivore(0.05, 500, 1, 0.01) {
    override fun toString() = "🐭"
}

class Goat : Herbivore(60.0, 140, 3, 10.0) {
    override fun toString() = "🐐"
}

class Sheep : Herbivore(70.0, 140, 3, 15.0) {
    override fun toString() = "🐑"
}

class Boar : Herbivore(400.0, 50, 2, 50.0) {
    override fun toString() = "🐗"
}

class Buffalo : Herbivore(700.0, 10, 3, 100.0) {
    override fun toString() = "🐃"
}

class Duck : Herbivore(1.0, 200, 4, 0.15) {
    override fun toString() = "🦆"
}

class Caterpillar : Herbivore(0.01, 1000, 0, 0.0) {
    override fun toString() = "🐛"
}