package nl.avwie.blog.webworkers

import kotlinx.browser.window
import org.w3c.dom.DedicatedWorkerGlobalScope
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.Window
import org.w3c.dom.Worker
import kotlin.random.Random

val isWorkerGlobalScope = js("typeof(WorkerGlobalScope) !== \"undefined\"") as Boolean

private fun mainScope(block: Window.() -> Unit) {
    if (!isWorkerGlobalScope) {
        block(window)
    }
}

private fun workerScope(block: DedicatedWorkerGlobalScope.() -> Unit) {
    if (isWorkerGlobalScope) {
        val self = js("self") as DedicatedWorkerGlobalScope
        block(self)
    }
}

fun <TPayload, TResult> launchJob(job: Job<TPayload, TResult>, payload: TPayload, onComplete: (TResult) -> Unit) {
    HybridApplication.instance?.launchJob(job, payload, onComplete)
}

class HybridApplication(val handlers: Map<String, Handler<*, *>>, val main: () -> Unit) {

    class Handler<TPayload, TResult>(private val job: Job<TPayload, TResult>)  {
        fun invoke(incoming: String): String {
            val payload = job.payloadEncoder.decode(incoming)
            val result = job.invoke(payload)
            return job.resultEncoder.encode(result)
        }
    }

    operator fun invoke() {
        mainScope {
            main()
        }

        workerScope {
            onmessage = { event ->
                console.log("Received message: " + event.data)
            }
        }
    }

    fun <TPayload, TResult> launchJob(job: Job<TPayload, TResult>, payload: TPayload, onComplete: (TResult) -> Unit) = mainScope {
        val scriptSrc = (document.currentScript as HTMLScriptElement).src
        val message = job.name + boundary + job.payloadEncoder.encode(payload)

        val worker = Worker(scriptSrc)
        worker.onmessage = { event ->
            val result = job.resultEncoder.decode(event.data as String)
            worker.terminate()
            onComplete(result)
        }
        worker.postMessage(message)
    }

    companion object {
        private val boundary = Random.nextLong().toString(16)

        var instance: HybridApplication? = null
            private set

        operator fun invoke(block: HybridApplicationBuilderScope.() -> Unit) {
            val builderScope = HybridApplicationBuilderScope()
            block(builderScope)

            val app = HybridApplication(builderScope.handlers, builderScope.main)
            instance = app
            app.invoke()
        }
    }
}

class HybridApplicationBuilderScope {
    val handlers = mutableMapOf<String, HybridApplication.Handler<*, *>>()

    var main: () -> Unit = {}

    fun <TPayload, TResult> registerJob(job: Job<TPayload, TResult>) {
        handlers[job.name] = HybridApplication.Handler(job)
    }
}