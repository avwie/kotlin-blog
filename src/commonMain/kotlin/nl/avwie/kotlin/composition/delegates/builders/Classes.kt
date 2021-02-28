package nl.avwie.kotlin.composition.delegates.builders

import nl.avwie.kotlin.composition.delegates.*

interface EntityBuilderProvider<T, B : EntityBuilder<T>> {
    fun builder(): B
}

interface EntityBuilder<T> {
    fun build(): T
}

fun <T, B : EntityBuilder<T>> build(provider: EntityBuilderProvider<T, B>, block: B.() -> Unit): T {
    val builder = provider.builder()
    block(builder)
    return builder.build()
}

class Player(
    val name: String,
    healthImpl: Health,
    spriteImpl: Sprite,
    dynamicsImpl: Dynamics,
    dangerousImpl: Dangerous
) :
    Drawable, Health by healthImpl, Sprite by spriteImpl, Dynamics by dynamicsImpl, Dangerous by dangerousImpl {

    companion object : EntityBuilderProvider<Player, PlayerBuilder> {
        override fun builder(): PlayerBuilder = PlayerBuilder()
    }
}

class PlayerBuilder : EntityBuilder<Player> {
    val sprite = SpriteImpl(ByteArray(0))

    var name = ""
    var health = 100
    var damage = 10
    var x = 0.0
    var y = 0.0
    var mass = 80.0

    override fun build(): Player =
        Player(name, HealthImpl(health), sprite, DynamicsImpl(mass, x to y), DangerousImpl(damage))
}



