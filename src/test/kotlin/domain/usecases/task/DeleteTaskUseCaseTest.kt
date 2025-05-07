package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskNotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
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
    fun `deleteTask should execute successfully when TaskRepository delete task`() {
       testScope.launch {
           val taskId = UUID.randomUUID().toString()
           coEvery { taskRepository.getAllTasks() } returns listOf(createTask(id = taskId))
           coEvery { taskRepository.deleteTask("",taskId) } returns Unit

           assertDoesNotThrow { deleteTaskUseCase.deleteTask("",taskId) }
       }
    }

    @Test
    fun `deleteTask should throw exception when TaskRepository throw exception`() {
     testScope.launch {
         val taskId = UUID.randomUUID().toString()
         coEvery { taskRepository.getAllTasks() } throws TaskNotFoundException()

         assertThrows<Exception> { deleteTaskUseCase.deleteTask("",taskId) }
     }
    }
}