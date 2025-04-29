package presentation

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.ConsoleOutputPrinter
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PlanMateConsoleUITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var planMateConsoleUI: PlanMateConsoleUI

    private val outputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setup() {
        inputReader = mockk(relaxed = true)
        System.setOut(PrintStream(outputStream))
        outputPrinter = ConsoleOutputPrinter()
        planMateConsoleUI = PlanMateConsoleUI(inputReader, outputPrinter)
    }

    @Test
    fun `should print welcome message when app starts`() {
        // when
        planMateConsoleUI.start()

        // then
        val printed = outputStream.toString().trim()
        assertThat(printed).contains("Welcome to PlanMate!")
    }
}