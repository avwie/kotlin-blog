package nl.avwie.kotlin.composition.components.native

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class NativeTests {

    @Test
    fun simple() {
        val player = Entity(Random.nextLong())
        player.setComponent(Components.Health(100))
        player.setComponent(Components.Dynamics(100.0, 5.0, 2.0))

        assertEquals(100, player.getComponent(Keys.Health)?.currentHealth)
        assertEquals(5.0 to 2.0, player.getComponent(Keys.Dynamics)?.position)
    }

    @Test
    fun sprite() {
        val world = Entity(Random.nextLong())
        world.setComponent(Components.Sprite("Foreground".encodeToByteArray(), SpriteTypeEnum.Foreground))
        world.setComponent(Components.Sprite("Background".encodeToByteArray(), SpriteTypeEnum.Background))

        assertEquals("Foreground", world.getComponent(Keys.Sprite(SpriteTypeEnum.Foreground))?.spriteData?.decodeToString())
        assertEquals("Background", world.getComponent(Keys.Sprite(SpriteTypeEnum.Background))?.spriteData?.decodeToString())
    }
}