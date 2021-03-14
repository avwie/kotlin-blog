package nl.avwie.bouncingballs

import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey

class DynamicsSystem<Id> : AbstractSystem<Id>() {
    override val keys: Set<ComponentKey<*>> = setOf(Dynamics)

    private var dt: Double = 0.0

    fun setElapsedTime(dt: Double) {
        this.dt = dt
    }

    override fun invoke(entity: Id) {
        backend.get(entity, Dynamics).also { dynamics ->
            dynamics.update(dt)
        }
    }
}