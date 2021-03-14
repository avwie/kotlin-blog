package nl.avwie.ecs

class SystemsRunner<Id>(val backend: Backend<Id>, vararg val systems: System<Id>) {

    init {
        systems.forEach { system ->
            system.backend = backend
        }
    }

    operator fun invoke() {
        systems.forEach { system ->
            system.beforeInvoke()
            backend.entities().forEach { entity ->
                if (system.keys.all { key -> backend.has(entity, key) }) {
                    system(entity)
                }
            }
            system.afterInvoke()
        }
    }
}