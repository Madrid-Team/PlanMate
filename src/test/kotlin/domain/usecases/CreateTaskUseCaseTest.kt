package domain.usecases

import com.google.common.truth.Truth.assertThat
import createTask
import domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
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
    fun `should return true when task is saved successfully`() {
        // given
        val task = createTask(title = "new task")
        every { taskRepository.createTask(task) } returns true

        // when
        val result = createTaskUseCase.createTask(task)

        // then
        verify { taskRepository.createTask(task) }
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when task saving fails`() {
        // given
        val task = createTask(title = "new task")
        every { taskRepository.createTask(task) } returns false

        // when
        val result = createTaskUseCase.createTask(task)

        // then
        verify { taskRepository.createTask(task) }
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when task title is empty`() {
        // given
        val task = createTask(title = "")

        // when
        val result = createTaskUseCase.createTask(task)

        // then
        verify(exactly = 0) { taskRepository.createTask(any()) }
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when repository throws exception`() {
        // given
        val task = createTask(title = "new task")
        every { taskRepository.createTask(task) } throws RuntimeException("error")

        // when
        val result = createTaskUseCase.createTask(task)

        // then
        assertThat(result).isFalse()
    }


}