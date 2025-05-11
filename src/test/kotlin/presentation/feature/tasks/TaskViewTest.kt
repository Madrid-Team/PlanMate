package presentation.feature.tasks

import domain.usecases.task.DisplayAllTasksUseCase
import domain.utils.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter


class TaskViewTest {
    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskView: TaskView
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        displayAllTasksUseCase = mockk()
        taskView = TaskView(displayAllTasksUseCase, outputPrinter, inputReader)
        testScope = TestScope()
    }

    @Test
    fun `should print project not found when project does not exist`() {
        testScope.launch {
            // Given
            val projectId = "non_existing_project"
            every { inputReader.readInput("Enter project ID: ") } returns projectId
            coEvery { displayAllTasksUseCase.display(projectId) } returns "Project not found."

            // When
            taskView.show()

            // Then
            verify { outputPrinter.printMessage("Project not found.") }
        }
    }

    @Test
    fun `should print formatted tasks when tasks exist`() {
        testScope.launch {
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
            coEvery { displayAllTasksUseCase.display(projectId) } returns expectedOutput

            // When
            taskView.show()

            // Then
            verify { outputPrinter.printMessage(expectedOutput) }
        }
    }

    @Test
    fun `should show error message when display fails`() {
        testScope.launch {
            val projectId = "project-123"
            val errorMessage = "Project not found"
            every { inputReader.readInput(any()) } returns projectId
            coEvery { displayAllTasksUseCase.display(projectId) } throws TaskExceptions(errorMessage)

            taskView.show()

            coVerifySequence {
                outputPrinter.printMessage("=== Display Tasks ===")
                inputReader.readInput("Enter project ID: ")
                displayAllTasksUseCase.display(projectId)
                outputPrinter.printMessage(errorMessage)
            }
        }
    }

    @Test
    fun `should display tasks when display is successful`() {
        testScope.launch {
            val projectId = "project-123"
            every { inputReader.readInput(any()) } returns projectId
            coEvery { displayAllTasksUseCase.display(projectId) } returns "Task 1\nTask 2"

            taskView.show()

            coVerifySequence {
                outputPrinter.printMessage("=== Display Tasks ===")
                inputReader.readInput("Enter project ID: ")
                displayAllTasksUseCase.display(projectId)
                outputPrinter.printMessage("Task 1\nTask 2")
            }
        }
    }


}