package presentation.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import kotlin.test.Test

class ConsoleInputReaderTest {

    private lateinit var consoleInputReader: ConsoleInputReader

    @BeforeEach
    fun setup() {
        consoleInputReader = ConsoleInputReader()
    }


    @Test
    fun `should return input when readInput is called`() {
        // given
        val input = "islam elhady"
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)

        // when
        val result = consoleInputReader.readInput("enter your name")

        // then
        assertThat(result).isEqualTo(input)
    }


    @Test
    fun `should return empty string when input is null`() {
        // given
        val inputStream = ByteArrayInputStream(byteArrayOf())
        System.setIn(inputStream)

        // when
        val result = consoleInputReader.readInput("enter your name")

        // then
        assertThat(result).isEqualTo("")
    }


}