package presentation.feature.tasks

import domain.usecases.task.DisplayAllTasksUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter


class TaskViewTest(){
    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskView: TaskView

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        displayAllTasksUseCase = mockk()
        taskView = TaskView(displayAllTasksUseCase, outputPrinter, inputReader)
    }
    @Test
    fun `should print project not found when project does not exist`() {
        // Given
        val projectId = "non_existing_project"
        every { inputReader.readInput("Enter project ID: ") } returns projectId
        every { displayAllTasksUseCase.display(projectId) } returns "Project not found."

        // When
        taskView.show()

        // Then
        verify { outputPrinter.printMessage("Project not found.") }
    }
    @Test
    fun `should print formatted tasks when tasks exist`() {
        // Given
        val projectId = "p1"
        val expectedOutput = """
            TODO:
            - Task 1

            In Progress:
            - Task 2

            Done:
            - Task 3
        """.trimIndent()

        every { inputReader.readInput("Enter project ID: ") } returns projectId
        every { displayAllTasksUseCase.display(projectId) } returns expectedOutput

        // When
        taskView.show()

        // Then
        verify { outputPrinter.printMessage(expectedOutput) }
    }
}