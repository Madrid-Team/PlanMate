package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetLogsUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getLogsUseCase: GetTaskLogsUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        getLogsUseCase = GetTaskLogsUseCase(taskRepository)
    }

    @Test
    fun `get TaskLogs should return true when task repository called and return success result`() {
        //Given
        val taskId = 1
        every { taskRepository.getTaskLogsByID(taskId.toString()) } returns Result.success(listOf<String>())

        //When
        val result = getLogsUseCase.getTaskLogs(taskId.toString())

        //Then
        kotlin.test.assertTrue { result.isSuccess }

    }

    @Test
    fun `get Task Logs should return false when project repository called and return failure result`() {
        //Given
        val projectId = 1
        every { taskRepository.getTaskLogsByID(projectId.toString()) } returns Result.failure(PlanMateExceptions(""))

        //When
        val result = getLogsUseCase.getTaskLogs(projectId.toString())

        //Then
        kotlin.test.assertFalse { result.isSuccess }

    }


}