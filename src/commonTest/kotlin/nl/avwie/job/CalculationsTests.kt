package nl.avwie.job

import kotlin.math.PI
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class CalculationsTests {

    @Test
    fun pi() {
        val r = approximatePI(10000000)
        assertTrue { abs(PI - r) < 0.01 }
    }
}