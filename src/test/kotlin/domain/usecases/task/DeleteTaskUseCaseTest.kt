package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `deleteTask should execute successfully when TaskRepository delete task`() {
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns Result.success(listOf(createTask(id = taskId)))
        every { taskRepository.deleteTask(taskId) } returns Unit

        assertDoesNotThrow { deleteTaskUseCase.deleteTask(taskId) }
    }

    @Test
    fun `deleteTask should throw exception when TaskRepository throw exception`() {
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } throws TaskNotFoundException()

        assertThrows<Exception> { deleteTaskUseCase.deleteTask(taskId) }
    }
}