package nl.avwie.bouncingballs

import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey

class GravitySystem<Id>(val g: Double) : AbstractSystem<Id>() {
    override val keys: Set<ComponentKey<*>> = setOf(Dynamics)

    override fun invoke(entity: Id) {
        backend.get(entity, Dynamics).also { dynamics ->
            dynamics.acceleration = vec2D.zero
            dynamics.applyForce(vec2D(0.0, g * dynamics.mass))
        }
    }
}