package presentation.feature.tasks

import domain.usecases.EditTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import kotlin.test.Test

class EditTaskCLITest {

    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var taskView: TaskView
    private lateinit var editTaskCLI: EditTaskCLI

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        editTaskUseCase = mockk()
        editTaskCLI = EditTaskCLI(inputReader, outputPrinter, taskView, editTaskUseCase)
    }

    @Test
    fun `should edit task successfully when call edit task`() {

        // Given
        every { inputReader.readInput(any()) } returnsMany listOf("1", "123", "New Title", "New Description")
        val updatedTask = helperTask(projectId = "1", id = "123", title = "New Title", description = "New Description")
        every { editTaskUseCase.editTask(updatedTask) } returns true

        // When
        editTaskCLI.show()

        // Then
        verify { outputPrinter.printMessage("Task updated successfully") }
    }
}