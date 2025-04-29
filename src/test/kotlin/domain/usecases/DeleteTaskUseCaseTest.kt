package domain.usecases

import createTask
import domain.repository.TaskRepository
import domain.utlis.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        every { taskRepository.getAllTasks() } returns listOf(createTask(id = taskId))

        //when
        deleteTaskUseCase.deleteTask(taskId)

        //then
        verify { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `should return throw exception when id is not found`() {
        //given
        val taskId = UUID.randomUUID().toString()
        every { taskRepository.getAllTasks() } returns listOf(createTask(id = taskId))

        //when && then
        assertThrows<TaskNotFoundException> {
            deleteTaskUseCase.deleteTask(taskId)
        }
    }
}