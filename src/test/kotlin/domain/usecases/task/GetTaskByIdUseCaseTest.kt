package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
    }

    private val taskId = "1"
    private val emptyTaskId = ""
    val task = createTask(id = "26d15335-54b5-48b1-8fd5-c6355f9324c9")

    @Test
    fun `should get task when id is not empty`() {
        runTest {
            coEvery { taskRepository.getTaskById(taskId) } returns task

            val result = getTaskByIdUseCase.getTaskById(taskId)

            assertThat(result).isEqualTo(task)
        }
    }

    @Test
    fun `should throw exception when id is empty`() {
        runTest {
            coEvery { taskRepository.getTaskById(emptyTaskId) } throws TaskExceptions.TaskNotFoundException("Task Id is empty")

            assertThrows<TaskExceptions.TaskNotFoundException> { getTaskByIdUseCase.getTaskById(emptyTaskId) }
        }
    }
}