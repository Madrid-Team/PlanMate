package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTasksByProjectIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }


    @Test
    fun `GetTasksByProjectIdUseCase should return the tasks when project id is found`() {
        val projectId = "12"
        val firstTask = createTask(projectId = projectId)
        val secondTask = createTask(projectId = projectId)
        every { taskRepository.getTasksByProjectId(projectId) } returns listOf(firstTask, secondTask)

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result)
    }

    @Test
    fun `GetTasksByProjectIdUseCase should throw exception when project id is not found`() {
        every { taskRepository.getAllTasks() } throws Exception()

        assertThrows<Exception> { getTasksByProjectIdUseCase(UUID.randomUUID().toString()) }
    }
}