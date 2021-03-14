package nl.avwie.ecs

interface ComponentKey<C>

interface Component<C> {
    val key: ComponentKey<C>
}

interface Backend<Id> {
    fun create(id: Id? = null): Id
    fun exists(id: Id): Boolean
    fun destroy(id: Id): Id
    fun entities(): Sequence<Id>

    fun <C : Component<C>> set(id: Id, component: C): C?
    fun has(id: Id, key: ComponentKey<*>): Boolean
    fun <C> get(id: Id, key: ComponentKey<C>): C
    fun <C> unset(id: Id, key: ComponentKey<C>): C
}