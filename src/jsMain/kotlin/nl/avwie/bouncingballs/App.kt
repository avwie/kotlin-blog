package nl.avwie.bouncingballs

import kotlinx.browser.document
import kotlinx.browser.window
import nl.avwie.ecs.ArrayBackend
import nl.avwie.ecs.HashMapBackend
import nl.avwie.ecs.ParallelSystem
import nl.avwie.ecs.SystemsRunner
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.url.URLSearchParams

fun main() {
    val canvas = document.getElementById("canvas") as? HTMLCanvasElement

    val params = URLSearchParams(window.location.search)

    canvas?.also {
        val app = BouncingBalls(canvas, params.get("count")?.toIntOrNull() ?: 10)
        app.start()
    }
}

class BouncingBalls(canvas: HTMLCanvasElement, count: Int) {

    val backend = ArrayBackend(count)
    val dynamicsSystem = DynamicsSystem<Int>()
    val gravitySystem = GravitySystem<Int>(300.0)
    val floorSystem = FloorSystem<Int>(canvas.height.toDouble())
    val spawnerAndDestroySystem = SpawnAndDestroySystem<Int>(
        bounds = rect2D(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble()),
        spawnTotal = count,
        spawnVelocity = 100.0 to 300.0
    )
    val drawingSystem = DrawingSystem<Int>(10.0, canvas)

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
