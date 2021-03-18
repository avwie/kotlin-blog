package nl.avwie.ecs

import nl.avwie.bouncingballs.Color
import nl.avwie.bouncingballs.Dynamics
import nl.avwie.common.datastructures.Deque

class ArrayBackend(val capacity: Int) : Backend<Int> {

    private val freedUpIndices = Deque<Int>()
    private var currentIndex = -1

    private val exists = BooleanArray(capacity) { false }
    private val colors = Array<Color?>(capacity) { null }
    private val dynamics = Array<Dynamics?>(capacity) { null }

    override fun create(id: Int?): Int {
        val idx = getIndex()
        exists[idx] = true
        return idx
    }

    private fun getIndex(): Int {
        if (!freedUpIndices.isEmpty()) {
            try {
                return freedUpIndices.popFront()!!
            } catch (exc: NullPointerException) {
                println()
            }
        }

        if (currentIndex >= capacity) {
            throw RuntimeException("Amount of entities exceed capacity")
        }

        currentIndex += 1
        return currentIndex
    }

    override fun exists(id: Int): Boolean = exists[id]

    override fun destroy(id: Int): Int {
        freedUpIndices.append(id)
        exists[id] = false
        return id
    }

    override fun entities(): Sequence<Int> {
        return exists.asSequence().mapIndexedNotNull { index, b -> if (b) index else null  }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C : Component<C>> set(id: Int, component: C): C? {
        val old = when (component.key) {
            is Color.Companion -> colors[id].also { colors[id] = component as Color }
            is Dynamics.Companion -> dynamics[id].also { dynamics[id] = component as Dynamics  }
            else -> throw IllegalArgumentException("Invalid component type")
        }
        return old as? C
    }

    override fun has(id: Int, key: ComponentKey<*>): Boolean {
        return when (key) {
            is Color.Companion -> colors[id] != null
            is Dynamics.Companion -> dynamics[id] != null
            else -> throw IllegalArgumentException("Invalid component type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C> get(id: Int, key: ComponentKey<C>): C {
        return when (key) {
            is Color.Companion -> colors[id]
            is Dynamics.Companion -> dynamics[id]
            else -> throw IllegalArgumentException("Invalid component type")
        } as C
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C> unset(id: Int, key: ComponentKey<C>): C {
        return when (key) {
            is Color.Companion -> colors[id]
            is Dynamics.Companion -> dynamics[id]
            else -> throw IllegalArgumentException("Invalid component type")
        } as C
    }
}