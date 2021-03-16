import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*

class HybridApplication(block: HybridApplicationBuilderScope.() -> Unit) {

    val run : () -> Unit = {
        val scope = HybridApplicationBuilderScope()
        block(scope)
        val action = scope.build()
        action()
    }
}

class HybridApplicationBuilderScope {
    private var browserBlock: Window.() -> Unit = {}
    private var workerBlock: DedicatedWorkerGlobalScope.() -> Unit = {}

    @Suppress("UNCHECKED_CAST")
    fun createWorker(name: String): Worker {
        val scriptSrc = (document.currentScript as HTMLScriptElement).src
        val options = js("({})")
        options["name"] = name
        return Worker(scriptSrc, options as WorkerOptions)
    }

    fun browser(block: Window.() -> Unit) {
        browserBlock = block
    }

    fun worker(block: DedicatedWorkerGlobalScope.() -> Unit) {
        workerBlock = block
    }

    fun build(): () -> Unit {
        if (js("typeof(WorkerGlobalScope) == \"undefined\"") as Boolean) {
            return {
                browserBlock(window)
            }
        } else {
            val self = js("self") as DedicatedWorkerGlobalScope
            return {
                workerBlock(self)
            }
        }
    }

    val DedicatedWorkerGlobalScope.name: String get() = self.asDynamic()["name"]
}