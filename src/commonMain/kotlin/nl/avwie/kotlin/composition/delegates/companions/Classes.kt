package nl.avwie.kotlin.composition.delegates.companions

import nl.avwie.kotlin.composition.delegates.*

class Player(
    val name: String,
    healthImpl: Health,
    spriteImpl: Sprite,
    dynamicsImpl: Dynamics,
    dangerousImpl: Dangerous
) :
    Drawable, Health by healthImpl, Sprite by spriteImpl, Dynamics by dynamicsImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(name: String, health: Int, position: Pair<Double, Double>, damage: Int): Player {
            return Player(name, HealthImpl(health), sprite, DynamicsImpl(74.0, position), DangerousImpl(damage))
        }
    }
}

class Orc(healthImpl: Health, spriteImpl: Sprite, dynamicsImpl: Dynamics, dangerousImpl: Dangerous) :
    Drawable, Health by healthImpl, Sprite by spriteImpl, Dynamics by dynamicsImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(position: Pair<Double, Double>, damage: Int): Orc {
            return Orc(HealthImpl(150), sprite, DynamicsImpl(120.0, position), DangerousImpl(damage))
        }
    }
}

class Tree(spriteImpl: Sprite, positionImpl: PositionImpl) :
    Drawable, Sprite by spriteImpl, Position by positionImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(position: Pair<Double, Double>): Tree {
            return Tree(sprite, PositionImpl(position))
        }
    }
}

class VenomousPlant(spriteImpl: Sprite, positionImpl: PositionImpl, dangerousImpl: Dangerous) :
    Drawable, Sprite by spriteImpl, Position by positionImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0))

        fun new(position: Pair<Double, Double>, damage: Int): VenomousPlant {
            return VenomousPlant(sprite, PositionImpl(position), DangerousImpl(damage))
        }
    }
}

class Potato(spriteImpl: Sprite, positionImpl: Position, edibleImpl: EdibleImpl) :
    Drawable, Sprite by spriteImpl, Position by positionImpl, Edible by edibleImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0))

        fun new(nutritionalValue: Int, position: Pair<Double, Double>): Potato {
            return Potato(sprite, PositionImpl(position), EdibleImpl(nutritionalValue))
        }
    }
}