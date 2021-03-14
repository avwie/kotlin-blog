package nl.avwie.ecs

import nl.avwie.common.Generator
import nl.avwie.ecs.generators.IntGenerator

class HashMapBackend<Id>(val idGenerator: Generator<Id>, val strict: Boolean = false) : Backend<Id> {
    private val components = mutableMapOf<Id, MutableMap<ComponentKey<*>, Component<*>>>()

    override fun create(id: Id?): Id = (id ?: idGenerator.generate()).also {
        require(!exists(it)) { "Entity $it already exists!" }
        components[it] = mutableMapOf()
    }

    override fun exists(id: Id): Boolean = components.containsKey(id)

    override fun destroy(id: Id): Id = id.also {
        requireExists(it)
        components.remove(it)
    }

    override fun entities(): Sequence<Id> = components.keys.asSequence()

    override fun <C : Component<C>> set(id: Id, component: C): C? {
        requireExists(id)
        val previous = components[id]!!.put(component.key, component) ?: return null
        return requireComponentIsType(previous, component.key)
    }

    override fun has(id: Id, key: ComponentKey<*>): Boolean {
        requireExists(id)
        return components[id]!!.containsKey(key)
    }

    override fun <C> get(id: Id, key: ComponentKey<C>): C {
        requireHas(id, key)
        return requireComponentIsType(components[id]!![key], key)
    }

    override fun <C> unset(id: Id, key: ComponentKey<C>): C {
        requireHas(id, key)
        return requireComponentIsType(components[id]!!.remove(key), key)
    }

    private fun requireExists(id: Id) {
        if (!strict) return
        require(exists(id)) { "Entity $id does not exist!" }
    }

    private fun requireHas(id: Id, key: ComponentKey<*>) {
        if (!strict) return
        requireExists(id)
        require(has(id, key)) { "Entity $id does not have a component with key $key"}
    }

    @Suppress("UNCHECKED_CAST")
    private fun <C> requireComponentIsType(component: Component<*>?, key: ComponentKey<C>): C {
        val casted = component as? C
        require(casted != null) { "Component $component is not of type expected by $key"}
        return casted
    }

    companion object {
        fun default(): Backend<Int> = HashMapBackend(IntGenerator(), strict = false)
    }
}