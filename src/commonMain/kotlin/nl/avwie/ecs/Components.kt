package nl.avwie.ecs

import nl.avwie.common.Vector2D

class Dynamics(
    val mass: Double,
    initialPosition: Vector2D<Double>,
    initialVelocity: Vector2D<Double>,
    initialAcceleration: Vector2D<Double>
): Component<Dynamics> {
    object Key : ComponentKey<Dynamics>
    override val key: ComponentKey<Dynamics> = Key

    var position = initialPosition
        private set

    var velocity = initialVelocity
        private set

    var acceleration = initialAcceleration
        private set

    fun applyForce(force: Vector2D<Double>) {
        acceleration += force / mass
    }

    fun update(dt: Double) {
        velocity += acceleration * dt
        position += velocity * dt
    }
}