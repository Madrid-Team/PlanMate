package presentation.feature.tasks

import com.google.common.truth.Truth.assertThat
import domain.usecases.task.GetTasksByProjectIdUseCase
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class TaskViewerTest {
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
    fun `should return tasks successfully`() {
        runTest {
            val projectId = UUID.randomUUID().toString()

            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } returns tasks

            val result = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)

            assertThat(result).isEqualTo(tasks)
        }
    }

    @Test
    fun `should print tasks grouped by state with correct format`() {
        taskViewer.printTasksByState(tasks)

        // Then
        verifyOrder {
            outputPrinter.printMessage("Tasks in state: TODO (1 tasks)")
            outputPrinter.printMessage("------------------------------")
            outputPrinter.printMessage("ID: $task1Id")
            outputPrinter.printMessage("Title: Task 1")
            outputPrinter.printMessage("Description: Description 1")
            outputPrinter.printMessage("Created by: user1")
            outputPrinter.printMessage("Logs: 2 entries")
            outputPrinter.printMessage("------------------------------")
            outputPrinter.printMessage("\n")

            outputPrinter.printMessage("Tasks in state: IN_PROGRESS (1 tasks)")
            outputPrinter.printMessage("------------------------------")
            outputPrinter.printMessage("ID: $task2Id")
            outputPrinter.printMessage("Title: Task 2")
            outputPrinter.printMessage("Description: Description 2")
            outputPrinter.printMessage("Created by: user2")
            outputPrinter.printMessage("Logs: 1 entries")
            outputPrinter.printMessage("------------------------------")
            outputPrinter.printMessage("\n")
        }
    }

    @Test
    fun `should print error message when TaskExceptions is thrown`() = runTest {
        // Given
        val projectId = UUID.randomUUID().toString()
        val errorMessage = "Project not found"

        coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } throws TaskExceptions(errorMessage)

        // When
        taskViewer.displayAllTasks(projectId)

        // Then
        verify { outputPrinter.printError(errorMessage) }
    }

    private val task1Id = UUID.randomUUID().toString()
    private val task2Id = UUID.randomUUID().toString()

    val tasks = listOf(
        helperTask(
            id = task1Id,
            title = "Task 1",
            description = "Description 1",
            state = "TODO",
            createdBy = "user1",
            logs = listOf("log1", "log2")
        ),
        helperTask(
            id = task2Id,
            title = "Task 2",
            description = "Description 2",
            state = "IN_PROGRESS",
            createdBy = "user2",
            logs = listOf("log1")
        )
    )
}