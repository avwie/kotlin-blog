package nl.avwie.bouncingballs

import nl.avwie.common.Vector2D
import nl.avwie.ecs.Component
import nl.avwie.ecs.ComponentKey
import kotlin.random.Random

class Dynamics(
    val mass: Double,
    initialPosition: Vector2D<Double> = vec2D.zero,
    initialVelocity: Vector2D<Double> = vec2D.zero,
    initialAcceleration: Vector2D<Double> = vec2D.zero
): Component<Dynamics> {
    companion object : ComponentKey<Dynamics>
    override val key: ComponentKey<Dynamics> = Dynamics

    var position = initialPosition
    var velocity = initialVelocity
    var acceleration = initialAcceleration

    fun applyForce(force: Vector2D<Double>) {
        acceleration += force / mass
    }

    fun update(dt: Double) {
        velocity += acceleration * dt
        position += velocity * dt
    }
}

data class Color(
    val red: Int,
    val green: Int,
    val blue: Int
): Component<Color> {

    companion object : ComponentKey<Color> {
        fun random(): Color = Color(Random.nextInt(0, 256), Random.nextInt(0, 256), Random.nextInt(0, 256))
    }
    override val key: ComponentKey<Color> = Color

    val hexCode = "#" + listOf(red, green, blue).joinToString(separator = "") { it.coerceIn(0, 255).toString(16) }

    override fun toString(): String {
        return hexCode
    }
}