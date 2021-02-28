package nl.avwie.kotlin.composition.delegates.companions

import nl.avwie.kotlin.composition.delegates.Drawable
import nl.avwie.kotlin.composition.delegates.builders.build
import nl.avwie.kotlin.composition.delegates.extensions.berserkAttack
import kotlin.test.Test
import kotlin.test.assertEquals

class ImplementationTests {

    val player = Player.new(name = "Beeblebrox", health = 100, position = 0.0 to 0.0, damage = 30)
    val orc = Orc.new(position = 5.0 to 0.0,  damage = 30)
    val tree = Tree.new(position = 15.0 to 25.0)
    val poisonIvy = VenomousPlant.new(position = 8.0 to 11.0, damage = 5)
    val entities : List<Any> = listOf(player, orc, tree, poisonIvy)

    @Test
    fun attackTest() {
        player.attack(orc)
        orc.attack(player)

        assertEquals(70, player.health)
        assertEquals(120, orc.health)
    }

    @Test
    fun dynamicsTest() {
        player.applyForce(5.0 to 0.0)
        player.updateDynamics(0.1)
        assertEquals(5.0 / 74.0  * 0.1, player.velocity.first)
    }

    @Test
    fun drawingTest() {
        entities.filterIsInstance<Drawable>().forEach { drawable ->
            println(drawable.position)
            println(drawable.spriteData)
        }
    }

    @Test
    fun extensionTest() {
        val berserkPlayer = Player.new(name = "Almost dead", health = 7, position = 0.0 to 0.0, damage = 30)
        val unfortunateOrc = Orc.new(position = 5.0 to 0.0,  damage = 30)

        println(unfortunateOrc.health) // prints 150
        berserkPlayer.berserkAttack(unfortunateOrc)
        println(unfortunateOrc.health) // prints 90
    }
}