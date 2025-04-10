package island

abstract class Entity {
    abstract fun act(cell: Cell)
    abstract override fun toString(): String
}