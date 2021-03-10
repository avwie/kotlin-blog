package nl.avwie.ecs

import nl.avwie.common.UUID

interface ComponentKey<C>

interface Component {
    val key: ComponentKey<*>
}

interface ECS {
    fun create(id: UUID? = null): UUID
    fun exists(id: UUID): Boolean
    fun destroy(id: UUID): UUID

    fun set(id: UUID, component: Component)
    fun has(id: UUID, key: ComponentKey<*>): Boolean
    fun <C> get(id: UUID, key: ComponentKey<C>): C
    fun <C> unset(id: UUID, key: ComponentKey<C>): C
}

class HashMapECS : ECS {
    private val components = mutableMapOf<UUID, MutableMap<ComponentKey<*>, Component>>()

    override fun create(id: UUID?): UUID = (id ?: UUID.random()).also {
        require(!exists(it)) { "Entity $it already exists!" }
        components[it] = mutableMapOf()
    }

    override fun exists(id: UUID): Boolean = components.containsKey(id)

    override fun destroy(id: UUID): UUID = id.also {
        requireExists(it)
        components.remove(it)
    }

    override fun set(id: UUID, component: Component) {
        requireExists(id)
        components[id]!![component.key] = component
    }

    override fun has(id: UUID, key: ComponentKey<*>): Boolean {
        requireExists(id)
        return components[id]!!.containsKey(key)
    }

    override fun <C> get(id: UUID, key: ComponentKey<C>): C {
        requireHas(id, key)
        return requireComponentIsType(components[id]!![key], key)
    }

    override fun <C> unset(id: UUID, key: ComponentKey<C>): C {
        requireHas(id, key)
        return requireComponentIsType(components[id]!!.remove(key), key)
    }

    private fun requireExists(id: UUID) {
        require(exists(id)) { "Entity $id does not exist!" }
    }

    private fun requireHas(id: UUID, key: ComponentKey<*>) {
        requireExists(id)
        require(has(id, key)) { "Entity $id does not have a component with key $key"}
    }

    @Suppress("UNCHECKED_CAST")
    private fun <C> requireComponentIsType(component: Component?, key: ComponentKey<C>): C {
        val casted = component as? C
        require(casted != null) { "Component $component is not of type expected by $key"}
        return casted
    }
}