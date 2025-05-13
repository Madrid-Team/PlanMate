package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import domain.utils.ProjectExceptions
import domain.utils.TaskExceptions
import io.mockk.coVerify
import io.mockk.coEvery
import domain.utils.TaskExceptions.TaskTitleIsEmptyException
import java.util.*

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getProjectByIdUseCase = mockk()
        getTaskByIdUseCase = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository, getProjectByIdUseCase, getTaskByIdUseCase)
        testScope = TestScope()
    }

    @Test
    fun `should complete successfully when task exists and is deleted`() {
        testScope.runTest {
            val projectId = UUID.randomUUID().toString()
            val taskId = UUID.randomUUID().toString()

            // Given
            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { getTaskByIdUseCase.getTaskById(projectId, taskId) } returns mockk()
            coEvery { taskRepository.deleteTask(taskId) } returns Unit

            // When + Then
            assertDoesNotThrow {
                deleteTaskUseCase.deleteTask(projectId, taskId)
            }

            coVerify(exactly = 1) { getProjectByIdUseCase.getById(projectId) }
            coVerify(exactly = 1) { getTaskByIdUseCase.getTaskById(projectId, taskId) }
            coVerify(exactly = 1) { taskRepository.deleteTask(taskId) }
        }

    }

    @Test
    fun `should throw TaskNotFoundException when task does not exist`() {
        testScope.runTest {
            val taskId = UUID.randomUUID().toString()
            val expectedException = TaskExceptions.TaskNotFoundException("Task not found")

            coEvery { taskRepository.deleteTask(taskId) } throws expectedException

            val thrown = assertThrows<TaskExceptions.TaskNotFoundException> {
                deleteTaskUseCase.deleteTask(taskId)
            }

            assert(thrown.message == "Task not found")
            coVerify(exactly = 1) { taskRepository.deleteTask(taskId) }
        }
    }

}