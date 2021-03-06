package nl.avwie.kotlin.composition.components.reflection

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class ReflectionTests {

    @Test
    fun simple() {
        val player = Entity(Random.nextLong())
        player.setComponent(Health(100))
        player.setComponent(Dynamics(100.0, 5.0, 2.0))

        assertEquals(100, player.getComponent(Health::class)?.currentHealth)
        assertEquals(5.0 to 2.0, player.getComponent<Dynamics>()?.position)
    }

    @Test
    fun query() {
        val player = Entity(1)
        player.setComponent(Health(100))
        player.setComponent(Dynamics(100.0, 5.0, 2.0))
        player.setComponent(Sprite(ByteArray(0)))

        val monster = Entity(2)
        monster.setComponent(Health(130))
        monster.setComponent(Dynamics(150.0, 10.0, -4.0))
        monster.setComponent(Sprite(ByteArray(0)))

        val background = Entity(3)
        monster.setComponent(Sprite(ByteArray(0)))

        val entities = listOf(player, monster, background)

        // draw all entities
        entities.forEach { entity ->
            entity.query(Dynamics::class, Sprite::class) { dynamics, sprite ->
                println("Drawing entity ${entity.id} on position x=${dynamics.position.first}, y=${dynamics.position.second}")
            } ?: println("Entity ${entity.id} doesn't have the required components")
        }
    }
}