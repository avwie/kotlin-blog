package nl.avwie.ecs.extensions

import nl.avwie.common.UUID
import nl.avwie.ecs.ComponentKey
import nl.avwie.ecs.ECS

fun ECS.destroyOrNull(id: UUID): UUID? = if (exists(id)) destroy(id) else null
fun <C> ECS.getOrNull(id: UUID, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) get(id, key) else null
fun <C> ECS.unsetOrNull(id: UUID, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) unset(id, key) else null