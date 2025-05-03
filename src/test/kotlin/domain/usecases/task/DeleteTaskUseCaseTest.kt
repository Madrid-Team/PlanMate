package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `should delete task when id is found`() {
        //given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns Result.success(listOf(createTask(id = taskId)))
        every { taskRepository.deleteTask(taskId) } returns Result.success(Unit)

        //when
        val result = deleteTaskUseCase.deleteTask(taskId)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return throw exception when id is not found`() {
        //given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } throws  TaskNotFoundException()

        //when && then
        assertThrows<TaskNotFoundException> {
            deleteTaskUseCase.deleteTask(taskId)
        }
    }
}