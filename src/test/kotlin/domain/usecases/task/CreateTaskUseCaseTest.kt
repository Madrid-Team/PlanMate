package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskTitleIsEmptyException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
        testScope = TestScope()
    }

    @Test
    fun `createTask should execute successfully when task is saved successfully`() {
        testScope.launch {
            val task = createTask(title = "new task", description = "description")
            coEvery { taskRepository.createTask(task) } returns Unit

            assertDoesNotThrow { createTaskUseCase.createTask(task) }
        }
    }

    @Test
    fun `createTask should throw exception when task saving fails`() {
        testScope.launch {
            // given
            val task = createTask(title = "new task")
            coEvery { taskRepository.createTask(task) } throws Exception()

            // when && then
            assertThrows<Exception> {
                taskRepository.createTask(task)
            }
        }
    }

    @Test
    fun `createTask should return false when task title is empty`() {
       testScope.launch {
           // given
           val task = createTask(title = "")
           coEvery { taskRepository.createTask(task) } throws TaskTitleIsEmptyException()

           // when && then
           assertThrows<TaskTitleIsEmptyException> {
               taskRepository.createTask(task)
           }
       }
    }
}