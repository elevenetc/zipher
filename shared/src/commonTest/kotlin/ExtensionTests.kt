import com.elevenetc.zipher.shared.containsIdx
import com.elevenetc.zipher.shared.forEachFrom
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ExtensionTests {
    @Test
    fun containsIdx() {
        assertEquals("123".containsIdx(-1), false)
        assertEquals("123".containsIdx(0), true)
        assertEquals("123".containsIdx(1), true)
        assertEquals("123".containsIdx(2), true)
        assertEquals("123".containsIdx(3), false)

        assertEquals("".containsIdx(0), false)
    }

    @Test
    fun forEachFrom() {
        "123".forEachFrom(0, verify("123"))
        "123".forEachFrom(1, verify("23"))
        "123".forEachFrom(2, verify("3"))
        "123".forEachFrom(3, verify(""))
        "123".forEachFrom(4, verify(""))

        assertFails { "123".forEachFrom(0, verify("321")) }
        assertFails { "123".forEachFrom(0, verify("124")) }
        assertFails { "123".forEachFrom(0, verify("")) }
    }
}