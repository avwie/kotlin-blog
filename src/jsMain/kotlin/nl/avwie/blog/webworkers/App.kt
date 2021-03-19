package nl.avwie.blog.webworkers

fun main() = HybridApplication {
    registerJob(MultiplicationJob)
    registerJob(GreeterJob)

    main = {
        setTimeout({
            launchJob(GreeterJob, payload = "world") { result ->
                println("Received result: $result")
            }
        }, 1000)


        setTimeout({
            launchJob(MultiplicationJob, 10 to 2) { result ->
                println("Received result: $result")
            }
        }, 2000)
    }
}