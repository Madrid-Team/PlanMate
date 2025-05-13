package presentation.feature.mate

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI
import presentation.utils.*
import kotlin.test.Test

class MateCLITest {

    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskCLI: TaskCLI
    private lateinit var projectCLI: ProjectCLI
    private lateinit var mateCLI: MateCLI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        taskCLI = mockk(relaxed = true)
        projectCLI = mockk(relaxed = true)
        mateCLI = MateCLI(inputReader, outputPrinter, taskCLI, projectCLI)
    }

    @Test
    fun `showMateMenu should call taskCLI show when selection is 1`() = runTest {
        every { inputReader.readInput(String.selectOption) } returnsMany listOf(
            String.selectionOne,
            String.selectionZero
        )

        mateCLI.showMateMenu()

        coVerifySequence {
            outputPrinter.printMenuItems(
                listOf(
                    String.mateMenu,
                    String.viewTaskMenu,
                    String.viewProjects,
                    String.logout
                )
            )
            inputReader.readInput(String.selectOption)
            taskCLI.show()
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
        }
    }

    @Test
    fun `showMateMenu should call projectCLI showProjects when selection is 2`() = runTest {
        every { inputReader.readInput(String.selectOption) } returnsMany listOf(
            String.selectionTwo,
            String.selectionZero
        )

        mateCLI.showMateMenu()

        coVerifySequence {
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
            projectCLI.showProjects()
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
        }
    }

    @Test
    fun `showMateMenu should return when selection is 0`() = runTest {
        every { inputReader.readInput(String.selectOption) } returns String.selectionZero

        mateCLI.showMateMenu()

        verifySequence {
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
        }

        confirmVerified(taskCLI, projectCLI)
    }

    @Test
    fun `showMateMenu should show error on invalid input`() = runTest {
        every { inputReader.readInput(String.selectOption) } returnsMany listOf("invalid", String.selectionZero)

        mateCLI.showMateMenu()

        verifySequence {
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
            outputPrinter.printError(String.invalidOption)
            outputPrinter.printMenuItems(any())
            inputReader.readInput(String.selectOption)
        }
    }
}
