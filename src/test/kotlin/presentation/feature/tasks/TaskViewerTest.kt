package presentation.feature.tasks

import com.google.common.truth.Truth.assertThat
import domain.usecases.task.GetTasksByProjectIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
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
            val tasks = listOf(
                helperTask(
                    id = UUID.randomUUID().toString(),
                    title = "Task 1",
                    description = "Description 1",
                    state = "TODO",
                    projectId = projectId
                ),
                helperTask(
                    id = UUID.randomUUID().toString(),
                    title = "Task 2",
                    description = "Description 2",
                    state = "IN_PROGRESS",
                    projectId = projectId
                ),
                helperTask(
                    id = UUID.randomUUID().toString(),
                    title = "Task 3",
                    description = "Description 3",
                    state = "IN_PROGRESS",
                    projectId = projectId
                ),
            )

            coEvery { getTasksByProjectIdUseCase.getTaskByProjectId(projectId) } returns tasks

            val result = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)

            assertThat(result).isEqualTo(tasks)
        }
    }
}