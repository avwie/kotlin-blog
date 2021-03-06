package nl.avwie.kotlin.composition.components.native

interface Key<C>

interface HasKey<C> {
    val key: Key<C>
}

enum class SpriteTypeEnum {
    Background,
    Foreground
}

object Keys {
    object Health : Key<Components.Health>
    data class Sprite(val type: SpriteTypeEnum) : Key<Components.Sprite>
    object Dynamics : Key<Components.Dynamics>
}

object Components {

    class Health(initialAmount: Int) : HasKey<Health> {
        override val key: Key<Health> = Keys.Health

        var currentHealth = initialAmount
            private set

        val isDead: Boolean get() = currentHealth <= 0

        fun damage(amount: Int) {
            currentHealth -= amount
        }
    }

    class Sprite(val spriteData: ByteArray, val type: SpriteTypeEnum) : HasKey<Sprite> {
        override val key: Key<Sprite> = Keys.Sprite(type)
    }

    class Dynamics(
        val mass: Double,
        sx0: Double,
        sy0: Double,
        vx0: Double = 0.0,
        vy0: Double = 0.0,
        ax0: Double = 0.0,
        ay0: Double = 0.0
    ) : HasKey<Dynamics> {
        override val key: Key<Dynamics> = Keys.Dynamics

        var position = sx0 to sy0
            private set

        var velocity = vx0 to vy0
            private set

        var acceleration = ax0 to ay0
            private set

        fun applyForce(amount: Pair<Double, Double>) {
            val (ax, ay) = acceleration
            val (fx, fy) = amount
            acceleration = ax + fx / mass to ay + fy / mass
        }

        fun updateDynamics(dt: Double) {
            val (ax, ay) = acceleration
            val (vx, vy) = velocity
            val (sx, sy) = position

            // this is clearly naive, since this is not deterministic, however... it serves the purpose for this post
            velocity = vx + ax * dt to vy + ay * dt
            position = sx + vx * dt to sy + vy * dt
        }
    }
}

interface ComponentHolder {
    fun setComponent(component: HasKey<*>)
    fun <C> getComponent(key: Key<C>): C?
}

class MapComponentHolder : ComponentHolder {
    private val components = mutableMapOf<Key<*>, Any>()

    override fun setComponent(component: HasKey<*>) {
        components[component.key] = component
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C> getComponent(key: Key<C>): C? {
        return components[key] as? C
    }
}

class Entity(val id: Long, private val components: ComponentHolder = MapComponentHolder()) :
    ComponentHolder by components