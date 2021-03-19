package nl.avwie.blog.webworkers

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.DedicatedWorkerGlobalScope
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.Window
import org.w3c.dom.Worker

val isWorkerGlobalScope = js("typeof(WorkerGlobalScope) !== \"undefined\"") as? Boolean  ?: throw IllegalStateException("Boolean cast went wrong")

private fun mainScope(block: Window.() -> Unit) {
    if (!isWorkerGlobalScope) {
        block(window)
    }
}

private fun workerScope(block: DedicatedWorkerGlobalScope.() -> Unit) {
    if (isWorkerGlobalScope) {
        val self = js("self") as? DedicatedWorkerGlobalScope ?: throw IllegalStateException("DedicatedWorkerGlobalScope cast went wrong")
        block(self)
    }
}

fun <TPayload, TResult> launchJob(job: Job<TPayload, TResult>, payload: TPayload, onComplete: (TResult) -> Unit) {
    HybridApplication.instance?.launchJob(job, payload, onComplete)
}

class HybridApplication(val handlers: Map<String, Handler<*, *>>, val main: Window.() -> Unit) {

    class Handler<TPayload, TResult>(private val job: Job<TPayload, TResult>)  {
        fun invoke(incoming: String): String {
            val payload = job.payloadEncoder.decode(incoming)
            val result = job.invoke(payload)
            return job.resultEncoder.encode(result)
        }
    }

    operator fun invoke() {
        mainScope {
            main(this)
        }

        workerScope {
            onmessage = { event ->
                val data = event.data as? String ?: throw IllegalStateException("String cast went wrong")
                val (jobName, payload) = parseMessage(data)
                val handler = handlers[jobName]!!
                val result = handler.invoke(payload)
                val message = generateMessage(jobName, result)
                postMessage(message)
            }
        }
    }

    fun <TPayload, TResult> launchJob(job: Job<TPayload, TResult>, payload: TPayload, onComplete: (TResult) -> Unit) = mainScope {
        val message = generateMessage(job.name, job.payloadEncoder.encode(payload))

        val worker = Worker(scriptSrc ?: throw IllegalStateException("Script src is null!"))
        worker.onmessage = { event ->
            val data = event.data as? String  ?: throw IllegalStateException("String cast went wrong")
            val (_, resultData) = parseMessage(data)
            val result = job.resultEncoder.decode(resultData)
            worker.terminate()
            onComplete(result)
        }
        worker.postMessage(message)
    }

    private fun generateMessage(jobName: String, payload: String): String {
        return jobName + boundary + payload
    }

    private fun parseMessage(message: String): Pair<String, String> {
        val (job, payload) = message.split(boundary)
        return job to payload
    }

    companion object {
        private val boundary = "___64cf1e111ef74542aed854eb2b926acf___"
        private val scriptSrc: String? = if (!isWorkerGlobalScope) (document.currentScript as? HTMLScriptElement)?.src else null

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

    var main: Window.() -> Unit = {}

    fun <TPayload, TResult> registerJob(job: Job<TPayload, TResult>) {
        handlers[job.name] = HybridApplication.Handler(job)
    }
}