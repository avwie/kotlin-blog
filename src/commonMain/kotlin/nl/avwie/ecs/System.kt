package nl.avwie.ecs

import nl.avwie.common.UUID

interface System {
    val keys : Set<ComponentKey<*>>
    var backend: Backend

    fun beforeInvoke()
    operator fun invoke(entity: UUID)
    fun afterInvoke()
}

