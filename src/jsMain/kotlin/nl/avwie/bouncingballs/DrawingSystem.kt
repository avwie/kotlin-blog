package nl.avwie.bouncingballs

import nl.avwie.ecs.AbstractSystem
import nl.avwie.ecs.ComponentKey
import nl.avwie.ecs.extensions.query
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

class DrawingSystem<Id>(val radius: Double, val ctx: CanvasRenderingContext2D) : AbstractSystem<Id>() {
    override val keys: Set<ComponentKey<*>> = setOf(Color, Dynamics)

    override fun beforeInvoke() {
        ctx.save()
        ctx.clearRect(0.0, 0.0, ctx.canvas.width.toDouble(), ctx.canvas.height.toDouble())
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
    }
}