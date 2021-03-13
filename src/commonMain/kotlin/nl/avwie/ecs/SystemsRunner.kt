package nl.avwie.ecs

class SystemsRunner(val backend: Backend, vararg val systems: System) {

    init {
        systems.forEach { system ->
            system.setBackend(backend)
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