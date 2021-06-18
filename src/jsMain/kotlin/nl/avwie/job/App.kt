package nl.avwie.job

fun main() = HybridApplication {
    registerJobDefinition(PIApproximationJobDefinition)

    main = {
        val job0 = Job(PIApproximationJobDefinition, payload = 1000000) { result ->
            println("Received result: $result")
        }

        val job1 = Job(PIApproximationJobDefinition, payload = 10000000) { result ->
            println("Received result: $result")
        }

        val job2 = Job(PIApproximationJobDefinition, payload = 100000000) { result ->
            println("Received result: $result")
        }

        setTimeout({
            launchJob(job0)
            launchJob(job1)
            launchJob(job2)
        }, 1000)
    }
}