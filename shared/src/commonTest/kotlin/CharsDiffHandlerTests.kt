import com.elevenetc.zipher.shared.CharsDiffHandler
import kotlin.test.Test

class CharsDiffHandlerTests {
    @Test
    fun `zed df`() {
        val diffHandler = CharsDiffHandler()
        diffHandler.handler = {
            println(it)
        }

        diffHandler.update("a")
        diffHandler.update("ab")
        diffHandler.update("abc")
        diffHandler.update("abc")
    }
}