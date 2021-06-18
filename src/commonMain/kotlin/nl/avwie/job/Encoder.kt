package nl.avwie.job

interface Encoder<In, Out> {
    fun encode(input: In): Out
    fun decode(data: Out): In

    companion object {
        val String = object : Encoder<String, String> {
            override fun encode(input: String): String = input
            override fun decode(data: String): String = data
        }

        val Int = object : Encoder<Int, String> {
            override fun encode(input: Int): String = input.toString()
            override fun decode(data: String): Int = data.toInt()
        }

        val Double = object : Encoder<Double, String> {
            override fun encode(input: Double): String = input.toString()
            override fun decode(data: String): Double = data.toDouble()
        }

        fun <L, R> Pair(l: Encoder<L, String>, r: Encoder<R, String>, seperator: String = ":") : Encoder<Pair<L, R>, String> = object :
            Encoder<Pair<L, R>, String> {
            override fun encode(input: Pair<L, R>): String = l.encode(input.first) + seperator + r.encode(input.second)
            override fun decode(data: String): Pair<L, R> = data.split(seperator).let { (sl, sr) -> l.decode(sl) to r.decode(sr) }
        }
    }
}