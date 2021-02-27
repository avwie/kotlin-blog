package nl.avwie.kotlin.composition.delegates

interface Health {
    val health: Int
    val isDead: Boolean

    fun damage(amount: Int)
    fun replenish(amount: Int)
}

interface Sprite {
    val spriteData: ByteArray
}

interface Position {
    val position: Pair<Double, Double>
}

interface Dynamics : Position {
    val mass: Double
    val velocity: Pair<Double, Double>
    val acceleration: Pair<Double, Double>

    fun applyForce(amount: Pair<Double, Double>)
    fun updateDynamics(dt: Double)
}

interface Drawable : Sprite, Position

interface Dangerous {
    val damage: Int

    fun attack(other: Health)
}