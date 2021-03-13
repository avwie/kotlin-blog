package nl.avwie.ecs

import nl.avwie.common.UUID
import kotlin.test.*

abstract class BackendTests {
    abstract fun createBackend(): Backend

    @Test
    fun create() {
        val ecs = createBackend()
        val e1 = ecs.create()
        val e2 = ecs.create()
        assertNotEquals(e1, e2)
        assertTrue { ecs.exists(e1) }
        assertTrue { ecs.exists(e2) }
        assertFalse { ecs.exists(UUID.random()) }
    }

    @Test
    fun createDoubleFails() {
        val ecs = createBackend()
        val e1 = ecs.create()
        assertFails {
            ecs.create(e1)
        }
    }

    @Test
    fun set() {
        val ecs = createBackend()
        val e1 = ecs.create()

        assertFalse { ecs.has(e1, Name.Key) }
        assertFalse { ecs.has(e1, Health.Key) }

        ecs.set(e1, Name("Foo")).also { previous ->
            assertNull(previous)
        }
        ecs.set(e1, Health(100)).also { previous ->
            assertNull(previous)
        }

        assertTrue { ecs.has(e1, Name.Key) }
        assertEquals("Foo", ecs.get(e1, Name.Key).name)
        assertTrue { ecs.has(e1, Health.Key) }
        assertEquals(100, ecs.get(e1, Health.Key).health)
        ecs.set(e1, Name("Bar")).also { previous ->
            assertNotNull(previous)
            assertEquals("Foo", previous.name)
        }
    }
}