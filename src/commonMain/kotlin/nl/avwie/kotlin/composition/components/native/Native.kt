package nl.avwie.kotlin.composition.components.native

interface ComponentKey<C>

data class ParameterizedComponentKey<C, T>(val parameter: T): ComponentKey<C>

interface Component<C> {
    val key: ComponentKey<C>
}

class Health(initialAmount: Int) : Component<Health> {
    override val key = Key

    var currentHealth = initialAmount
        private set

    val isDead: Boolean get() = currentHealth <= 0

    fun damage(amount: Int) {
        currentHealth -= amount
    }

    object Key : ComponentKey<Health>
}

enum class SpriteTypeEnum {
    Foreground,
    Background;
}

class Sprite(val spriteData: ByteArray, val type: SpriteTypeEnum) : Component<Sprite> {
    override val key = Key[type]

    object Key {
        operator fun get(type: SpriteTypeEnum): ComponentKey<Sprite> = ParameterizedComponentKey(type)
    }
}

class Dynamics(
    val mass: Double,
    sx0: Double,
    sy0: Double,
    vx0: Double = 0.0,
    vy0: Double = 0.0,
    ax0: Double = 0.0,
    ay0: Double = 0.0
) : Component<Dynamics> {
    override val key = Key

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

    object Key : ComponentKey<Dynamics>
}

interface ComponentHolder {
    fun setComponent(component: Component<*>)
    fun <C> getComponent(key: ComponentKey<C>): C?
}

class MapComponentHolder : ComponentHolder {
    private val components = mutableMapOf<ComponentKey<*>, Any>()

    override fun setComponent(component: Component<*>) {
        components[component.key] = component
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C> getComponent(key: ComponentKey<C>): C? {
        return components[key] as? C
    }
}

class Entity(val id: Long, private val components: ComponentHolder = MapComponentHolder()) :
    ComponentHolder by components