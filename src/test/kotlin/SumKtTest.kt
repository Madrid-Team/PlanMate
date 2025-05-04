import com.google.common.truth.Truth.assertThat
import org.madrid.sum
import kotlin.test.Test

class SumKtTest {
    @Test
    fun `should return sum of two numbers`() {
        val result = sum(2, 2)
        assertThat(result).isEqualTo(4)
    }
}
