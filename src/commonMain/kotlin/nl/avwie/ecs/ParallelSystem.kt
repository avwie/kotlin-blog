package nl.avwie.ecs

class ParallelSystem<Id>(vararg val systems: System<Id>) : AbstractSystem<Id>() {

    override val keys: Set<ComponentKey<*>> = systems.fold(setOf()) { acc, system -> acc + system.keys }

    override var backend: Backend<Id>
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

    override fun invoke(entity: Id) {
        systems.forEach { system ->
            system.invoke(entity)
        }
    }
}