package nl.avwie.bouncingballs

import nl.avwie.ecs.HashMapBackend
import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicsTests {

    @Test
    fun update() {
        val dynamics = Dynamics(1.0, initialVelocity = vec2D(1.0, 0.0))
        assertEquals(0.0, dynamics.position.x)

        (0 until 1000).forEach {
            dynamics.update(0.01)
        }

        assertEquals(10.0, dynamics.position.x)
    }
}