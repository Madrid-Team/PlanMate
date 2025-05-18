package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.TaskNotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTasksByProjectIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
        testScope = TestScope()
    }

    @Test
    fun `should return tasks when tasks exist for project`() {
        testScope.runTest {
            // Given
            val projectId = "valid_project_id"
            val tasks = listOf(
                createTask(id = UUID.randomUUID().toString(), title = "Task 1", description = "Description 1"),
                createTask(id = UUID.randomUUID().toString(), title = "Task 2", description = "Description 2")
            )
            coEvery { taskRepository.getTasksByProjectId(projectId) } returns tasks

            // When & Then
            assertDoesNotThrow {
                getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
            }

            coVerify(exactly = 1) { taskRepository.getTasksByProjectId(projectId) }
        }
    }

    @Test
    fun `should throw TaskNotFoundException when no tasks exist for project`() {
        testScope.runTest {
            // Given
            val projectId = "valid_project_id"
            coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

            // When & Then
            assertThrows<TaskNotFoundException> {
                getTasksByProjectIdUseCase.getTaskByProjectId(projectId)
            }

            coVerify(exactly = 1) { taskRepository.getTasksByProjectId(projectId) }
        }
    }


}