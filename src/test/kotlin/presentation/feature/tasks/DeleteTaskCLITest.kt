package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import domain.utlis.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class DeleteTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var deleteTaskCLI: DeleteTaskCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        deleteTaskUseCase = mockk()
        deleteTaskCLI = DeleteTaskCLI(inputReader, outputPrinter, deleteTaskUseCase)
        testScope = TestScope()
    }

    @Test
    fun `should show success message when task is deleted successfully`() {
        testScope.launch {
            // given
            every { inputReader.readInput() } returns "1"
            coEvery { deleteTaskUseCase("1", "2") } returns Unit

            // when
            deleteTaskCLI.show()

            // then
            coVerify {
                deleteTaskUseCase("1", "2")
                outputPrinter.printMessage("=== Delete Task ===")
                outputPrinter.printMessage("Enter task ID to delete:")
                deleteTaskUseCase("1", "2")
                outputPrinter.printMessage("Task deleted successfully.")
            }
        }
    }

    @Test
    fun `should show fail message when task deletion fails`() {
        // given
        testScope.launch {
            every { inputReader.readInput() } returns "2"
            coEvery { deleteTaskUseCase("2", "1") } throws TaskExceptions.TaskCannotDeleteException()

            // when
            deleteTaskCLI.show()

            // then
            verify {
                outputPrinter.printError(
                    TaskExceptions.TaskCannotDeleteException().message ?: "Task not found or could not be deleted."
                )
            }
        }
    }
}