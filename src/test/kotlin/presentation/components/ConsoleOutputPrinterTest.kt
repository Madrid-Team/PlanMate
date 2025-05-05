package presentation.components

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class ConsoleOutputPrinterTest {
    private lateinit var outputPrinter: ConsoleOutputPrinter
    private val outputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setup() {
        outputPrinter = ConsoleOutputPrinter()
        System.setOut(PrintStream(outputStream))
    }

    @Test
    fun `should print message to console`() {
        // given
        val message = "welcome to plane mate"

        // when
        outputPrinter.printMessage(message)

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).isEqualTo(message)

    }


    @Test
    fun `should print empty message to console`() {
        // given
        val emptyMessage = ""

        // when
        outputPrinter.printMessage(emptyMessage)

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).isEqualTo(emptyMessage)
    }

    @Test
    fun `should print message with multiple new lines correctly`() {
        // given
        val messageWithNewLines = "line 1\nline 2\nline 3"

        // when
        outputPrinter.printMessage(messageWithNewLines)

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).isEqualTo(messageWithNewLines)
    }

    @Test
    fun `should print empty error message to console`() {
        // given
        val error = "something error"

        // when
        outputPrinter.printError(error)

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).isEqualTo("[ERROR]: $error")
    }


}