package presentation.feature.tasks

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskCLITest {
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var createTaskCLI: CreateTaskCLI
    private lateinit var deleteTaskCLI: DeleteTaskCLI
    private lateinit var editTaskCLI: EditTaskCLI
    private lateinit var taskView: TaskView
    private lateinit var taskCLI: TaskCLI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        createTaskCLI = mockk(relaxed = true)
        deleteTaskCLI = mockk(relaxed = true)
        editTaskCLI = mockk(relaxed = true)
        taskView = mockk(relaxed = true)
        taskCLI = TaskCLI(createTaskCLI,editTaskCLI,deleteTaskCLI,taskView,outputPrinter,inputReader)
    }

    @Test
    fun `should navigate to CreateProjectCLI when user selects 1`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("1", "0")

        // when
        taskCLI.show()

        // then
        verify { createTaskCLI.show() }
    }

    @Test
    fun `should navigate to EditTaskCLI when user selects 2`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("2", "0")

        // when
        taskCLI.show()

        // then
        verify { editTaskCLI.show() }
    }

    @Test
    fun `should navigate to DeleteTaskCLI when user selects 3`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("3", "0")

        // when
        taskCLI.show()

        // then
        verify { deleteTaskCLI.show() }
    }

    @Test
    fun `should navigate to TaskView when user selects 4`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("4", "0")

        // when
        taskCLI.show()

        // then
        verify { taskView.show() }
    }

    @Test
    fun `should print invalid option message when user selects unknown option`() {
        // given
        every { inputReader.readInput(any()) } returnsMany listOf("5", "0")

        // when
        taskCLI.show()

        // then
        verify { outputPrinter.printError("Invalid option.") }
    }
}