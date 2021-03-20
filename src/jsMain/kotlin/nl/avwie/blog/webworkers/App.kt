package nl.avwie.blog.webworkers

fun main() = HybridApplication {
    registerJobDefinition(MultiplicationJobDefinition)
    registerJobDefinition(GreeterJobDefinition)

    main = {
        val job1 = Job(GreeterJobDefinition, payload = "world") { result ->
            println("Received result: $result")
        }

        val job2 = Job(MultiplicationJobDefinition, 10 to 2) { result ->
            println("Received result: $result")
        }

        setTimeout({ launchJob(job1) }, 1000)
        setTimeout({ launchJob(job2) }, 2000)
    }
}