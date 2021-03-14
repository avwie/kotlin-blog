package nl.avwie.bouncingballs

import kotlinx.browser.document
import kotlinx.browser.window
import nl.avwie.ecs.HashMapBackend
import nl.avwie.ecs.ParallelSystem
import nl.avwie.ecs.SystemsRunner
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

fun main() {
    val canvas = document.getElementById("canvas") as? HTMLCanvasElement
    val ctx = canvas?.getContext("2d") as? CanvasRenderingContext2D

    ctx?.also {
        val app = BouncingBalls(ctx)
        app.start()
    }
}

class BouncingBalls(ctx: CanvasRenderingContext2D) {

    val backend = HashMapBackend.default()
    val dynamicsSystem = DynamicsSystem<Int>()
    val gravitySystem = GravitySystem<Int>(300.0)
    val floorSystem = FloorSystem<Int>(ctx.canvas.height.toDouble())
    val spawnerAndDestroySystem = SpawnAndDestroySystem<Int>(
        bounds = rect2D(0.0, 0.0, ctx.canvas.width.toDouble(), ctx.canvas.height.toDouble()),
        spawnTotal = 500,
        spawnVelocity = 100.0 to 300.0
    )
    val drawingSystem = DrawingSystem<Int>(10.0, ctx)

    val runner = SystemsRunner(backend,
        spawnerAndDestroySystem,
        ParallelSystem(gravitySystem, dynamicsSystem, floorSystem),
        drawingSystem
    )

    private var lastTimeStamp = 0.0
    private var dt = 0.0

    fun start() {
        lastTimeStamp = window.performance.now()
        window.requestAnimationFrame(::loop)
    }

    private fun loop(timestamp: Double) {
        dt = (timestamp - lastTimeStamp)
        lastTimeStamp = timestamp
        dynamicsSystem.setElapsedTime(dt / 1000.0)
        runner.invoke()
        window.requestAnimationFrame(::loop)
    }
}
