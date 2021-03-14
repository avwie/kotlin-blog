package nl.avwie.ecs

abstract class AbstractSystem<Id> : System<Id> {
    override lateinit var backend: Backend<Id>

    override fun beforeInvoke() {}
    override fun afterInvoke() {}
}