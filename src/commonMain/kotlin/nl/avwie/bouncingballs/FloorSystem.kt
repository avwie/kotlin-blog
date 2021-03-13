package nl.avwie.bouncingballs

import nl.avwie.common.UUID
import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey

class FloorSystem(
    val height: Double
) : AbstractSystem() {
    override val keys: Set<ComponentKey<*>> = setOf(Dynamics)

    override fun invoke(entity: UUID) {
        backend.get(entity, Dynamics).also { dynamics ->
            if (dynamics.position.y > height) {
                dynamics.position = vec2D(dynamics.position.x, 2 * height - dynamics.position.y)
                dynamics.velocity = vec2D(dynamics.velocity.x, -dynamics.velocity.y)
            }
        }
    }
}