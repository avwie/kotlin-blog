package nl.avwie.ecs

import nl.avwie.common.UUID

class ParallelSystem(vararg val systems: System) : AbstractSystem() {

    override val keys: Set<ComponentKey<*>> = systems.fold(setOf()) { acc, system -> acc + system.keys }

    override var backend: Backend
        get() = super.backend
        set(value) {
            systems.forEach { system ->
                system.backend = value
            }
            super.backend = value
        }

    override fun beforeInvoke() {
        systems.forEach { system ->
            system.beforeInvoke()
        }
    }

    override fun afterInvoke() {
        systems.forEach { system ->
            system.afterInvoke()
        }
    }

    override fun invoke(entity: UUID) {
        systems.forEach { system ->
            system.invoke(entity)
        }
    }
}