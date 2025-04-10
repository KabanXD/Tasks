package island

class Cell(val x: Int, val y: Int, val island: Island) {
    val entities: MutableList<Entity> = mutableListOf()

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun simulate() {
        entities.toList().forEach { it.act(this) }
    }

    override fun toString() = "($x,$y)"
}