package nl.avwie.ecs

class HashMapBackendTests : BackendTests() {
    override fun createBackend(): Backend {
        return HashMapBackend()
    }
}