package nl.avwie.bouncingballs

import nl.avwie.common.UUID
import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey

class GravitySystem(val g: Double) : AbstractSystem() {
    override val keys: Set<ComponentKey<*>> = setOf(Dynamics)

    override fun invoke(entity: UUID) {
        backend.get(entity, Dynamics).also { dynamics ->
            dynamics.acceleration = vec2D.zero
            dynamics.applyForce(vec2D(0.0, g * dynamics.mass))
        }
    }
}