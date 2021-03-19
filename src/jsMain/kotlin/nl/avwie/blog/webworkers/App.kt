package nl.avwie.blog.webworkers

fun main() = HybridApplication {
    registerJob(MultiplicationJob)
    registerJob(GreeterJob)

    main = {
        launchJob(MultiplicationJob, 10 to 2) { result ->
            println("Received result: $result")
        }
    }
}