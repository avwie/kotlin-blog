package nl.avwie.blog.webworkers

interface Job<TPayload, TResult> {
    val name: String
    val payloadEncoder: Encoder<TPayload, String>
    val resultEncoder: Encoder<TResult, String>

    operator fun invoke(payload: TPayload): TResult
}

object MultiplicationJob : Job<Pair<Int, Int>, Int> {
    override val name = "multiplication"
    override val payloadEncoder = Encoder.Pair(Encoder.Int, Encoder.Int)
    override val resultEncoder = Encoder.Int

    override fun invoke(payload: Pair<Int, Int>): Int = payload.first * payload.second
}

object GreeterJob : Job<String, String> {
    override val name = "greet"
    override val payloadEncoder = Encoder.String
    override val resultEncoder = Encoder.String

    override fun invoke(payload: String): String = "Hello $payload!!!"
}