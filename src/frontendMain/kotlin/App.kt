import org.w3c.dom.DedicatedWorkerGlobalScope
import org.w3c.dom.Worker

fun main() {
    if (js("typeof(WorkerGlobalScope) == \"undefined\"") as Boolean) {
        console.log("In normal scope")
        val worker = Worker("kotlin-blog.js")
        worker.postMessage("Hello")
    } else {
        val self = js("self") as DedicatedWorkerGlobalScope
        self.onmessage = { messageEvent ->
            println("Received from main: " + messageEvent.data)
        }
    }
}