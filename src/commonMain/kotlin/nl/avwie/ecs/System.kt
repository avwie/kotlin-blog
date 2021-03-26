package nl.avwie.ecs

interface System<Id> {
    val keys : Set<ComponentKey<*>>
    var backend: Backend<Id>

    fun beforeInvoke()
    operator fun invoke(entity: Id)
    fun afterInvoke()
}

