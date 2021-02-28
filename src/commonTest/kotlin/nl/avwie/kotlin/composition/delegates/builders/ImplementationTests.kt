package nl.avwie.kotlin.composition.delegates.builders

import kotlin.test.Test

class ImplementationTests {

    @Test
    fun builderTest() {
        val player = build(Player) {
            name = "Beeblebrox"
            health = 120

            x = 3.0
            y = 4.0
        }
    }
}