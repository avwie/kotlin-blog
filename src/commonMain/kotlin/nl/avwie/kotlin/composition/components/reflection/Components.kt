package nl.avwie.kotlin.composition.components.reflection

import kotlin.reflect.KClass

interface Component

class Health(initialAmount: Int) : Component {
    var currentHealth = initialAmount
        private set

    val isDead: Boolean get() = currentHealth <= 0

    fun damage(amount: Int) {
        currentHealth -= amount
    }
}

class Sprite(val spriteData: ByteArray): Component

class Dynamics(
    val mass: Double,
    sx0: Double,
    sy0: Double,
    vx0: Double = 0.0,
    vy0: Double = 0.0,
    ax0: Double = 0.0,
    ay0: Double = 0.0
) : Component {
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


interface ComponentHolder {
    fun setComponent(component: Component)
    fun <C : Component> getComponent(type: KClass<C>): C?
}

class MapComponentHolder : ComponentHolder {
    private val components = mutableMapOf<KClass<out Component>, Component>()

    override fun setComponent(component: Component) {
        components[component::class] = component
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C : Component> getComponent(type: KClass<C>): C? {
        return components[type] as? C
    }
}

class Entity(val id: Long, private val components: ComponentHolder = MapComponentHolder()) :
    ComponentHolder by components

inline fun <reified C : Component> ComponentHolder.getComponent(): C? {
    return getComponent(C::class)
}

fun <C1, R> ComponentHolder.query(t1: KClass<C1>, block: (C1) -> R): R? where C1 : Component {
    val c1 = getComponent(t1) ?: return null
    return block(c1)
}

fun <C1, C2, R> ComponentHolder.query(t1: KClass<C1>, t2: KClass<C2>, block: (C1, C2) -> R): R?
where C1 : Component, C2 : Component {
    val c1 = getComponent(t1) ?: return null
    val c2 = getComponent(t2) ?: return null
    return block(c1, c2)
}

fun <C1, C2, C3, R> ComponentHolder.query(t1: KClass<C1>, t2: KClass<C2>, t3: KClass<C3>, block: (C1, C2, C3) -> R): R?
where C1 : Component, C2 : Component, C3 : Component {
    val c1 = getComponent(t1) ?: return null
    val c2 = getComponent(t2) ?: return null
    val c3 = getComponent(t3) ?: return null
    return block(c1, c2, c3)
}