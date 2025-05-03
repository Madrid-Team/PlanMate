package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import domain.utlis.TaskExceptions.NoLogsFoundException
import domain.utlis.TaskExceptions.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetLogsUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getLogsUseCase: GetLogsUseCase

    @BeforeEach
    fun setup(){
        taskRepository = mockk(relaxed = true)
        getLogsUseCase = GetLogsUseCase(taskRepository)
    }

    @Test
    fun `get task logs when task exists and logs are not empty`(){
        val taskId = "120"
        val logs = listOf(
            "Ahmed added a file",
            "Ahmed removed a file"
        )
        every { taskRepository.getTaskLogsByID(taskId) } returns logs

        val result = getLogsUseCase.getTaskLogs(taskId)

        assertThat(result).isEqualTo(logs)

    }

    @Test
    fun `get task logs should throw exceptions when the task exists but the logs are empty `(){
        val taskId = "120"

        every { taskRepository.getTaskLogsByID(taskId) } throws  NoLogsFoundException()

        assertThrows <NoLogsFoundException>{
            getLogsUseCase.getTaskLogs(taskId)
        }



    }

@Test
    fun `get task logs should throw exceptions when the task is not found`(){
        val taskId = "120"

        every { taskRepository.getTaskLogsByID(taskId) } throws TaskNotFoundException()


        assertThrows <TaskNotFoundException>{
            getLogsUseCase.getTaskLogs(taskId)
        }



    }





}