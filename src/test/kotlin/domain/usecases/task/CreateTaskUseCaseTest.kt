package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.CannotCreateTaskException
import domain.utlis.TaskExceptions.TaskTitleIsEmptyException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
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
    fun `createTask should return true when task is saved successfully`() {
        // given
        val task = createTask(title = "new task", description = "description")
        every { taskRepository.createTask(task) } returns Result.success(Unit)

        // when
        val result = createTaskUseCase.createTask(task)

        // then
        verify { taskRepository.createTask(task) }
        assertThat(result)
    }

    @Test
    fun `createTask should return false when task saving fails`() {
        // given
        val task = createTask(title = "new task")
        every { taskRepository.createTask(task) } throws CannotCreateTaskException()

        // when && then
        assertThrows<CannotCreateTaskException> {
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