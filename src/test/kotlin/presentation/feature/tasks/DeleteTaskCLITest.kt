package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utils.TaskCannotDeleteException
import domain.utils.TaskNotFoundException
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.deleteTaskException
import presentation.utils.deleteTaskHeader
import presentation.utils.deleteTaskSuccess
import presentation.utils.enterTaskIdToDelete
import kotlin.test.Test

class DeleteTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var deleteTaskCLI: DeleteTaskCLI
    private lateinit var testScope: TestScope
    private lateinit var coroutineScope: CoroutineScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        deleteTaskUseCase = mockk(relaxed = true)
        coroutineScope = CoroutineScope(Dispatchers.IO)
        deleteTaskCLI = DeleteTaskCLI(inputReader, outputPrinter, deleteTaskUseCase)
        testScope = TestScope()
    }


    @Test
    fun `should show success message when task is deleted successfully`() = testScope.runTest {
        // Given
        val taskId = "123"
        every { inputReader.readInput() } returns taskId
        coEvery { deleteTaskUseCase.deleteTaskByTaskId(taskId) } just Runs

        // When
        deleteTaskCLI.show()

        // Then
        coVerifySequence {
            outputPrinter.printMessage(String.deleteTaskHeader)
            outputPrinter.printMessage(String.enterTaskIdToDelete)
            inputReader.readInput()
            deleteTaskUseCase.deleteTaskByTaskId(taskId)
            outputPrinter.printMessage(String.deleteTaskSuccess)
        }
    }

    @Test
    fun `should show error message when deletion throws TaskExceptions`() = runTest {
        // Given
        val taskId = "123"
        val exception = TaskNotFoundException(taskId)
        every { inputReader.readInput() } returns taskId
        coEvery { deleteTaskUseCase.deleteTaskByTaskId(taskId) } throws exception

        // When
        deleteTaskCLI.show()

        // Then
        coVerifySequence {
            outputPrinter.printMessage(String.deleteTaskHeader)
            outputPrinter.printMessage(String.enterTaskIdToDelete)
            inputReader.readInput()
            deleteTaskUseCase.deleteTaskByTaskId(taskId)
            outputPrinter.printError(String.deleteTaskException.format(exception))
        }
    }


    @Test
    fun `should show fail message when task deletion fails`() {
        // given
        testScope.runTest {
            // Given
            val taskId = "456"
            val exception = TaskCannotDeleteException()
            every { inputReader.readInput() } returns taskId
            coEvery { deleteTaskUseCase.deleteTaskByTaskId(taskId) } throws exception

            // When
            deleteTaskCLI.show()

            // Then
            verify {
                outputPrinter.printError(String.deleteTaskException.format(exception))
            }
        }
    }
}