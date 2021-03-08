package nl.avwie.kotlin.composition.components.native

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class NativeTests {

    @Test
    fun simple() {
        val player = Entity(Random.nextLong())
        player.setComponent(Health(100))
        player.setComponent(Dynamics(100.0, 5.0, 2.0))

        assertEquals(100, player.getComponent(Health.Key)?.currentHealth)
        assertEquals(5.0 to 2.0, player.getComponent(Dynamics.Key)?.position)
    }

    @Test
    fun sprite() {
        val world = Entity(Random.nextLong())
        world.setComponent(Sprite("Foreground".encodeToByteArray(), SpriteTypeEnum.Foreground))
        world.setComponent(Sprite("Background".encodeToByteArray(), SpriteTypeEnum.Background))

        assertEquals("Foreground", world.getComponent(Sprite.Key[SpriteTypeEnum.Foreground])?.spriteData?.decodeToString())
        assertEquals("Background", world.getComponent(Sprite.Key[SpriteTypeEnum.Background])?.spriteData?.decodeToString())
    }

    @Test
    fun extensions() {
        val player = Entity(Random.nextLong())
        player += Health(100)
        player += Sprite(ByteArray(0), SpriteTypeEnum.Foreground)

        val position = player.getOrElse(Dynamics.Key) { Dynamics(100.0, 0.0, 0.0) }

        assertEquals(100, player[Health.Key]?.currentHealth)
        assertEquals(0.0, position.position.first)
    }
}