package presentation.feature.tasks

import domain.usecases.task.GetTasksByProjectIdUseCase
import domain.utils.ProjectExceptions
import domain.utils.TaskExceptions
import io.mockk.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter


class TaskViewTest {
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var inputReader: InputReader
    private lateinit var outputPrinter: OutputPrinter
    private lateinit var taskViewer: TaskViewer
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        inputReader = mockk()
        outputPrinter = mockk(relaxed = true)
        getTasksByProjectIdUseCase = mockk()
        taskViewer = TaskViewer(getTasksByProjectIdUseCase, inputReader, outputPrinter)
        testScope = TestScope()
    }

    @Test
    fun `should print project not found when project does not exist`() {
        runTest {
            // Given
            val projectId = "non_existing_project"
            every { inputReader.readInput("Enter project ID: ") } returns projectId
//            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } returns "Project not found."

            //When
            taskViewer.displayAllTasks(projectId)

            // Then
            verify { outputPrinter.printMessage("Project not found.") }
        }
    }

    @Test
    fun `should print formatted tasks when tasks exist`() {
        runTest {
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
//            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } returns expectedOutput

            // When
            taskViewer.displayAllTasks(projectId)

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
            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } throws TaskExceptions(errorMessage)

            taskViewer.displayAllTasks(projectId)

            coVerifySequence {
                outputPrinter.printMessage("=== Display Tasks ===")
                inputReader.readInput("Enter project ID: ")
                getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
                outputPrinter.printMessage(errorMessage)
            }
        }
    }

    @Test
    fun `should display tasks when display is successful`() {
        runTest {
            val projectId = "project-123"
            val tasksOutput = listOf(
                helperTask(title = "task 1"),
                helperTask(title = "task 2")
            )
            every { inputReader.readInput(any()) } returns projectId
            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } returns tasksOutput

            taskViewer.displayAllTasks(projectId)

            coVerifySequence {
                outputPrinter.printMessage("=== Display Tasks ===")
                inputReader.readInput("Enter project ID: ")
                getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
                outputPrinter.printMessage(any())
            }
        }
    }


    @Test
    fun `should print error message when ProjectExceptions is thrown`() {
        runTest {
            val projectId = "project-123"
            val exception = ProjectExceptions("Project not found")

            every { inputReader.readInput(any()) } returns projectId
            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } throws exception

            taskViewer.displayAllTasks(projectId)

            verify {
                outputPrinter.printMessage("=== Display Tasks ===")
                outputPrinter.printMessage("Project not found")
            }
        }
    }

}