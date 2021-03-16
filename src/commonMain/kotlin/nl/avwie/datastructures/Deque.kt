package nl.avwie.datastructures

class Deque<T> : Collection<T> {
    data class Node<T>(val element: T, var prev: Node<T>? = null, var next: Node<T>? = null) {
        override fun toString(): String = element.toString()
    }

    override var size: Int = 0
        private set

    private var front : Node<T>? = null
    private var back : Node<T>? = null

    fun prepend(el: T) {
        size++
        when {
            front == null -> {
                front = Node(el, prev = null, next = null)
            }
            back == null -> {
                back = front
                front = Node(el, prev = null, next = back)
                back!!.prev = front
            }
            else -> {
                Node(el, prev = null, next = front).also {
                    front!!.prev = it
                    front = it
                }
            }
        }
    }

    fun append(el: T) {
        size++
        when {
            front == null -> front = Node(el, prev = null, next = null)
            back == null -> {
                back = Node(el, prev = front, next = null)
                front!!.next = back
            }
            else -> {
                Node(el, prev = back, next = null).also {
                    back!!.next = it
                    back = it
                }
            }
        }
    }

    fun popFront(): T? {
        if (front != null) {
            return front?.element.also {
                size--
                front = front?.next
            }
        }
        return null
    }

    fun popBack(): T? {
        if (back != null) {
            return back?.element.also {
                size--
                back = if (back?.prev == front) null else back?.prev
            }
        } else if (front != null) {
            return front?.element.also {
                size--
                front = null
            }
        }
        return null
    }

    fun peekFront(): T? = front?.element
    fun peekBack(): T? {
        return if (back != null) back!!.element
        else peekFront()
    }

    fun clear() {
        size = 0
        front = null
        back = null
    }

    override fun contains(element: T): Boolean {
        val it = iterator()
        while (it.hasNext()) {
            if (it.next() == element) return true
        }
        return false
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        val set = elements.toMutableSet()
        val it = iterator()
        while (it.hasNext() && set.isNotEmpty()) {
            set.remove(it.next())
        }
        return set.isEmpty()
    }

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> = DequeIterator(this)

    fun reversed(): Iterator<T> = DequeIterator(this, true)

    class DequeIterator<T>(deque: Deque<T>, private val reversed: Boolean = false) : Iterator<T> {

        private var node = if (!reversed) deque.front else deque.back

        override fun hasNext(): Boolean = node != null

        override fun next(): T {
            return node!!.element.also {
                node = if (!reversed) node!!.next else node!!.prev
            }
        }
    }
}