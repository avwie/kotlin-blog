package nl.avwie.bouncingballs

import nl.avwie.common.Rectangle
import nl.avwie.common.UUID
import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey
import kotlin.random.Random

class SpawnAndDestroySystem(
    val bounds: Rectangle<Double>,
    val spawnTotal: Int,
    val spawnVelocity: Pair<Double, Double>
) : AbstractSystem() {
    override val keys: Set<ComponentKey<*>> = setOf(Dynamics)

    private var totalEntities: Int = 0
    private val entitiesToDestroy = mutableListOf<UUID>()

    override fun beforeInvoke() {
        totalEntities = 0
        entitiesToDestroy.clear()
    }

    override fun invoke(entity: UUID) {
        backend.get(entity, Dynamics).also { dynamics ->
            if (bounds.contains(dynamics.position)) {
                totalEntities += 1
            } else {
                entitiesToDestroy.add(entity)
            }
        }
    }

    override fun afterInvoke() {
        entitiesToDestroy.forEach { id ->
            backend.destroy(id)
        }

        val entitiesToSpawn = spawnTotal - totalEntities
        (0 until entitiesToSpawn).forEach { _ ->
            backend.create().also { id ->
                val ltr = Random.nextBoolean()
                val dynamics = Dynamics(
                    mass = 1.0,
                    initialPosition = vec2D(if (ltr) bounds.x0 else bounds.x1, Random.nextDouble(bounds.y0, bounds.y1)),
                    initialVelocity = vec2D(Random.nextDouble(spawnVelocity.first, spawnVelocity.second) * (if (ltr) 1.0 else -1.0), 0.0),
                    initialAcceleration = vec2D.zero
                )
                backend.set(id, dynamics)
                backend.set(id, Color.random())
            }
        }
    }
}