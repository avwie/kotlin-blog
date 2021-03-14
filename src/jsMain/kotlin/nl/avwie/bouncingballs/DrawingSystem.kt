package nl.avwie.bouncingballs

import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey
import nl.avwie.ecs.extensions.query
import org.w3c.dom.*
import kotlin.math.PI

class DrawingSystem<Id>(val radius: Double, val canvas: HTMLCanvasElement) : AbstractSystem<Id>() {
    override val keys: Set<ComponentKey<*>> = setOf(Color, Dynamics)

    private val offscreen = OffscreenCanvas(canvas.width, canvas.height)
    private val ctx: OffscreenCanvasRenderingContext2D = offscreen.getContext("2d") as OffscreenCanvasRenderingContext2D
    private val target: CanvasRenderingContext2D = canvas.getContext("2d") as CanvasRenderingContext2D

    override fun beforeInvoke() {
        ctx.save()
        target.clearRect(0.0, 0.0, canvas.width.toDouble(), ctx.canvas.height.toDouble())
        ctx.clearRect(0.0, 0.0, canvas.width.toDouble(), ctx.canvas.height.toDouble())
    }

    override fun invoke(entity: Id) {
        backend.query(entity, Dynamics, Color) { dynamics, color ->
            ctx.fillStyle = color.toString()
            ctx.beginPath()
            ctx.arc(dynamics.position.x, dynamics.position.y, radius, 0.0, 2 * PI)
            ctx.fill()
        }
    }

    override fun afterInvoke() {
        ctx.restore()
        target.drawImage(offscreen.transferToImageBitmap(), 0.0, 0.0)
    }
}