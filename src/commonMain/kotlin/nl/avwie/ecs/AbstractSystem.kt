package nl.avwie.ecs

abstract class AbstractSystem : System {
    override lateinit var backend: Backend

    override fun beforeInvoke() {}
    override fun afterInvoke() {}
}