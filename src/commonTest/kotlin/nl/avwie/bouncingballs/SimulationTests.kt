package nl.avwie.bouncingballs

import nl.avwie.ecs.ArrayBackend
import nl.avwie.ecs.ParallelSystem
import nl.avwie.ecs.SystemsRunner
import kotlin.test.Test

class SimulationTests {

    @Test
    fun long() {
        val count = 500
        val width = 1280
        val height = 800
        val backend = ArrayBackend(count)
        val dynamicsSystem = DynamicsSystem<Int>()
        val gravitySystem = GravitySystem<Int>(300.0)
        val floorSystem = FloorSystem<Int>(height.toDouble())
        val spawnerAndDestroySystem = SpawnAndDestroySystem<Int>(
            bounds = rect2D(0.0, 0.0, width.toDouble(), height.toDouble()),
            spawnTotal = count,
            spawnVelocity = 100.0 to 300.0
        )

        val runner = SystemsRunner(backend,
            spawnerAndDestroySystem,
            ParallelSystem(gravitySystem, dynamicsSystem, floorSystem)
        )

        val dt = 1.0 / 60
        dynamicsSystem.setElapsedTime(dt)
        var t = 0.0
        while (t < 10.0) {
            runner.invoke()
            t += dt
        }
    }
}