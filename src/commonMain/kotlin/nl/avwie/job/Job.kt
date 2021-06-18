package nl.avwie.job

data class Job<TPayload, TResult>(
    val definition: JobDefinition<TPayload, TResult>,
    val payload: TPayload,
    val onComplete: (TResult) -> Unit
)

interface JobDefinition<TPayload, TResult> {
    val name: String
    val payloadEncoder: Encoder<TPayload, String>
    val resultEncoder: Encoder<TResult, String>

    operator fun invoke(payload: TPayload): TResult
}

object PIApproximationJobDefinition : JobDefinition<Int, Double> {
    override val name: String = "approximate"
    override val payloadEncoder: Encoder<Int, String> = Encoder.Int
    override val resultEncoder: Encoder<Double, String> = Encoder.Double

    override fun invoke(payload: Int): Double = approximatePI(payload)
}