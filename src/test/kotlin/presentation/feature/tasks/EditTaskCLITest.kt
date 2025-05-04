package presentation.feature.tasks

import domain.usecases.task.EditTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.UUID
import kotlin.test.Test

class EditTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var taskView: TaskView
    private lateinit var editTaskCLI: EditTaskCLI

    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        taskView = mockk(relaxed = true)
        editTaskCLI = EditTaskCLI(inputReader, outputPrinter, taskView, editTaskUseCase)
    }

    @Test
    fun `should edit task successfully when call edit task`() {
        every { inputReader.readInput(any()) } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            "New Title",
            "New Description"
        )
        val updatedTask = helperTask(
            projectId = UUID.randomUUID().toString(),
            id = UUID.randomUUID().toString(),
            title = "New Title",
            description = "New Description"
        )
        every { editTaskUseCase.editTask(updatedTask) } returns Result.success(Unit)

        editTaskCLI.show()

        verify { outputPrinter.printMessage("Task updated successfully") }
    }

    @Test
    fun `should show error message when task update fails`() {
        every { inputReader.readInput(any()) } returnsMany listOf(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            "New Title",
            "New Description"
        )
        val updatedTask = helperTask(
            projectId = UUID.randomUUID().toString(),
            id = UUID.randomUUID().toString(),
            title = "New Title",
            description = "New Description"
        )
        every { editTaskUseCase.editTask(any()) } returns Result.failure(Exception())

        editTaskCLI.show()

        verify { outputPrinter.printError("Failed to update task") }

    }
}