package nl.avwie.ecs.extensions

import nl.avwie.common.UUID
import nl.avwie.ecs.ComponentKey
import nl.avwie.ecs.Backend

fun Backend.destroyOrNull(id: UUID): UUID? = if (exists(id)) destroy(id) else null
fun <C> Backend.getOrNull(id: UUID, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) get(id, key) else null
fun <C> Backend.unsetOrNull(id: UUID, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) unset(id, key) else null

fun <C1> Backend.query(id: UUID, k1: ComponentKey<C1>, block: (C1) -> Unit) = block(get(id, k1))
fun <C1, C2> Backend.query(id: UUID, k1: ComponentKey<C1>, k2: ComponentKey<C2>, block: (C1, C2) -> Unit) = block(get(id, k1), get(id, k2))