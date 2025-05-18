package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.NoLogsFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetTaskLogsUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskLogsUseCase: GetTaskLogsUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTaskLogsUseCase = GetTaskLogsUseCase(taskRepository)
        testScope = TestScope()
    }


    @Test
    fun `should return logs when task logs exist`() {
        testScope.runTest {
            // Given
            val taskId = "valid_task_id"
            val logs = listOf("Task created", "Task updated")
            coEvery { taskRepository.getTaskLogsByTaskId(taskId) } returns logs

            // When & Then
            assertDoesNotThrow {
                getTaskLogsUseCase.getTaskLogsByTaskId(taskId)
            }

            coVerify(exactly = 1) { taskRepository.getTaskLogsByTaskId(taskId) }
        }
    }

    @Test
    fun `should throw NoLogsFoundException when no logs are found`() {
        testScope.runTest {
            // Given
            val taskId = "valid_task_id"
            coEvery { taskRepository.getTaskLogsByTaskId(taskId) } returns emptyList()

            // When & Then
            assertThrows<NoLogsFoundException> {
                getTaskLogsUseCase.getTaskLogsByTaskId(taskId)
            }

            coVerify(exactly = 1) { taskRepository.getTaskLogsByTaskId(taskId) }
        }
    }

}