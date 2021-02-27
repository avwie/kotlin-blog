package nl.avwie.kotlin.composition.delegates

class HealthImpl(initialHealth: Int): Health {
    override var health: Int = initialHealth
        private set

    override val isDead: Boolean get() = health <= 0

    override fun damage(amount: Int) {
        health -= amount
    }

    override fun replenish(amount: Int) {
        health += amount
    }
}

class SpriteImpl(override val spriteData: ByteArray): Sprite

class PositionImpl(override val position: Pair<Double, Double>): Position

class DynamicsImpl(override val mass: Double, initialPosition: Pair<Double, Double>): Dynamics {
    override var position: Pair<Double, Double> = initialPosition
        private set

    override var velocity: Pair<Double, Double> = 0.0 to 0.0
        private set

    override var acceleration: Pair<Double, Double> = 0.0 to 0.0
        private set

    override fun applyForce(amount: Pair<Double, Double>) {
        val (ax, ay) = acceleration
        val (fx, fy) = amount
        acceleration = ax + fx / mass to ay + fy / mass
    }

    override fun updateDynamics(dt: Double) {
        val (ax, ay) = acceleration
        val (vx, vy) = velocity
        val (sx, sy) = position

        velocity = vx + ax * dt to vy + ay * dt
        position = sx + vx * dt to sy + vy * dt
    }
}

class DangerousImpl(override val damage: Int) : Dangerous {
    override fun attack(other: Health) {
        other.damage(damage)
    }
}

class Player( val name: String, healthImpl: Health, spriteImpl: Sprite, dynamicsImpl: Dynamics, dangerousImpl: Dangerous):
    Drawable, Health by healthImpl, Sprite by spriteImpl, Dynamics by dynamicsImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(name: String, health: Int, position: Pair<Double, Double>, damage: Int): Player {
            return Player(name, HealthImpl(health), sprite, DynamicsImpl(74.0, position), DangerousImpl(damage))
        }
    }
}

class Orc(healthImpl: Health, spriteImpl: Sprite, dynamicsImpl: Dynamics, dangerousImpl: Dangerous):
    Drawable, Health by healthImpl, Sprite by spriteImpl, Dynamics by dynamicsImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(position: Pair<Double, Double>, damage: Int): Orc {
            return Orc(HealthImpl(150), sprite, DynamicsImpl(120.0, position), DangerousImpl(damage))
        }
    }
}

class Tree(spriteImpl: Sprite, positionImpl: PositionImpl):
    Drawable, Sprite by spriteImpl, Position by positionImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0)) // just for mocking purposes

        fun new(position: Pair<Double, Double>): Tree {
            return Tree(sprite, PositionImpl(position))
        }
    }
}

class VenomousPlant(spriteImpl: Sprite, positionImpl: PositionImpl, dangerousImpl: Dangerous):
    Drawable, Sprite by spriteImpl, Position by positionImpl, Dangerous by dangerousImpl {

    companion object {
        val sprite = SpriteImpl(ByteArray(0))

        fun new(position: Pair<Double, Double>, damage: Int): VenomousPlant {
            return VenomousPlant(sprite, PositionImpl(position), DangerousImpl(damage))
        }
    }
}