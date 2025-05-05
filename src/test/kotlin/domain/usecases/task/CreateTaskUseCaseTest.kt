package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.TaskTitleIsEmptyException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `createTask should execute successfully when task is saved successfully`() {
        val task = createTask(title = "new task", description = "description")
        every { taskRepository.createTask(task) } returns Unit

        assertDoesNotThrow { createTaskUseCase.createTask(task) }
    }

    @Test
    fun `createTask should throw exception when task saving fails`() {
        // given
        val task = createTask(title = "new task")
        every { taskRepository.createTask(task) } throws Exception()

        // when && then
        assertThrows<Exception> {
            taskRepository.createTask(task)
        }
    }

    @Test
    fun `createTask should return false when task title is empty`() {
        // given
        val task = createTask(title = "")
        every { taskRepository.createTask(task) } throws TaskTitleIsEmptyException()

        // when && then
        assertThrows<TaskTitleIsEmptyException> {
            taskRepository.createTask(task)
        }
    }
}