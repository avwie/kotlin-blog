package nl.avwie.ecs

abstract class AbstractSystem : System {
    protected lateinit var backend: Backend

    override fun setBackend(backend: Backend) {
        this.backend = backend
    }

    override fun beforeInvoke() {}
    override fun afterInvoke() {}
}