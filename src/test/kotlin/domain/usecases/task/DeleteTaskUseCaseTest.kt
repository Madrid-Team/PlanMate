package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `deleteTask should return success when TaskRepository return success`() {
        //given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns Result.success(listOf(createTask(id = taskId)))
        every { taskRepository.deleteTask(taskId) } returns Result.success(Unit)

        //when
        val result = deleteTaskUseCase.deleteTask(taskId)

        //then
        assertTrue { result.isSuccess }
    }

    @Test
    fun `deleteTask should return failure when TaskRepository return failure`() {
        //given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } throws TaskNotFoundException()

        val result = deleteTaskUseCase.deleteTask(taskId)

        assertTrue { result.isFailure }
    }
}