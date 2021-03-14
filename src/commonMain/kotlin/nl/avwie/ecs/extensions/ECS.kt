package nl.avwie.ecs.extensions

import nl.avwie.common.UUID
import nl.avwie.ecs.ComponentKey
import nl.avwie.ecs.Backend

fun <Id> Backend<Id>.destroyOrNull(id: Id): Id? = if (exists(id)) destroy(id) else null
fun <Id, C> Backend<Id>.getOrNull(id: Id, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) get(id, key) else null
fun <Id, C> Backend<Id>.unsetOrNull(id: Id, key: ComponentKey<C>): C? = if (exists(id) && has(id, key)) unset(id, key) else null

fun <Id, C1> Backend<Id>.query(id: Id, k1: ComponentKey<C1>, block: (C1) -> Unit) = block(get(id, k1))
fun <Id, C1, C2> Backend<Id>.query(id: Id, k1: ComponentKey<C1>, k2: ComponentKey<C2>, block: (C1, C2) -> Unit) = block(get(id, k1), get(id, k2))