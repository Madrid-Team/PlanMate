package presentation.feature.tasks

import domain.usecases.task.EditTaskUseCase
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import kotlin.test.Test

class EditTaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var taskView: TaskView
    private lateinit var editTaskCLI: EditTaskCLI
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk(relaxed = true)
        outputPrinter = mockk(relaxed = true)
        editTaskUseCase = mockk(relaxed = true)
        taskView = mockk(relaxed = true)
        editTaskCLI = EditTaskCLI(inputReader, outputPrinter, editTaskUseCase)
        testScope = TestScope()
    }

    @Test
    fun `should edit task successfully when call edit task`() {
        testScope.launch {
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
            coEvery { editTaskUseCase(updatedTask) } returns Unit

            editTaskCLI.show()

            verify { outputPrinter.printMessage("Task updated successfully") }
        }
    }

    @Test
    fun `should show error message when task update fails`() {
        testScope.launch {
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
            coEvery { editTaskUseCase(any()) } throws TaskExceptions.TaskCannotEditException()

            editTaskCLI.show()

            verify { outputPrinter.printError(TaskExceptions.TaskCannotEditException().message!!) }
        }
    }
}