package nl.avwie.kotlin.composition.delegates

class HealthImpl(initialHealth: Int) : Health {
    override var health: Int = initialHealth
        private set

    override val isDead: Boolean get() = health <= 0

    override fun damage(amount: Int) {
        health -= amount
    }

    override fun eat(edible: Edible) {
        health += edible.nutritionalValue
    }
}

class EdibleImpl(override val nutritionalValue: Int) : Edible

class SpriteImpl(override val spriteData: ByteArray) : Sprite

class PositionImpl(override val position: Pair<Double, Double>) : Position

class DynamicsImpl(override val mass: Double, initialPosition: Pair<Double, Double>) : Dynamics {
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