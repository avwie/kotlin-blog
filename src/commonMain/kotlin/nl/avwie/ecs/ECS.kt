package nl.avwie.ecs

import nl.avwie.common.UUID

interface ComponentKey<C>

interface Component<C> {
    val key: ComponentKey<C>
}

interface ECS {
    fun create(id: UUID? = null): UUID
    fun exists(id: UUID): Boolean
    fun destroy(id: UUID): UUID

    fun <C : Component<C>> set(id: UUID, component: C): C?
    fun has(id: UUID, key: ComponentKey<*>): Boolean
    fun <C> get(id: UUID, key: ComponentKey<C>): C
    fun <C> unset(id: UUID, key: ComponentKey<C>): C
}