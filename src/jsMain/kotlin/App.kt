fun main() {
    val app = HybridApplication {
        browser {
            val workers = (0 .. 3).map { i -> createWorker("worker-$i") }
            workers.forEachIndexed { i, worker ->
                worker.onmessage = { event ->
                    console.log("[browser] Received message from worker-$i: ${event.data}")
                }
                worker.postMessage("Hello worker $i!!!")
            }
        }

        worker {
            onmessage = { event ->
                console.log("[${name}] Received message: ${event.data}")
                setTimeout({
                    postMessage("$name sends a message back!")
                }, 2000)
            }
        }
    }
    app.run()
}