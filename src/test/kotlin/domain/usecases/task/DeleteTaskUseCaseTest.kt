package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.TaskExceptions
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

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
        testScope = TestScope()
    }

    @Test
    fun `should complete successfully when task exists and is deleted`() {
        testScope.runTest {
            val taskId = UUID.randomUUID().toString()
            coEvery { taskRepository.deleteTaskByTaskId(taskId) } returns Unit

            assertDoesNotThrow { deleteTaskUseCase.deleteTaskByTaskId(taskId) }
            coVerify(exactly = 1) { taskRepository.deleteTaskByTaskId(taskId) }
        }

    }

    @Test
    fun `should throw TaskNotFoundException when task does not exist`() {
        testScope.runTest {
            val taskId = UUID.randomUUID().toString()
            val expectedException = TaskExceptions.TaskNotFoundException("Task not found")

            coEvery { taskRepository.deleteTaskByTaskId(taskId) } throws expectedException

            val thrown = assertThrows<TaskExceptions.TaskNotFoundException> {
                deleteTaskUseCase.deleteTaskByTaskId(taskId)
            }

            assert(thrown.message == "Task not found")
            coVerify(exactly = 1) { taskRepository.deleteTaskByTaskId(taskId) }
        }
    }

}