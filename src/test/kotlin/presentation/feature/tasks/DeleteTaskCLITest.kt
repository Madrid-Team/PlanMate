package presentation.feature.tasks

import domain.usecases.task.DeleteTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class DeleteTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var taskView: TaskView
    private lateinit var deleteTaskCLI: DeleteTaskCLI

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        deleteTaskUseCase = mockk()
        deleteTaskCLI = DeleteTaskCLI(inputReader, outputPrinter, taskView, deleteTaskUseCase)
    }

    @Test
    fun `should show success message when task is deleted successfully`() {
        // given
        every { inputReader.readInput() } returns "1"
        every { deleteTaskUseCase.deleteTask("1") } returns true

        // when
        deleteTaskCLI.show()

        // then
        verify {
            outputPrinter.printMessage("=== Delete Task ===")
            outputPrinter.printMessage("Enter task ID to delete:")
            deleteTaskUseCase.deleteTask("1")
            outputPrinter.printMessage("Task deleted successfully")
        }
    }

    @Test
    fun `should show fail message when task deletion fails`() {
        // given
        every { inputReader.readInput() } returns "2"
        every { deleteTaskUseCase.deleteTask("2") } returns false

        // when
        deleteTaskCLI.show()

        // then
        verify { outputPrinter.printMessage("Task not found or could not be deleted.") }
    }
}