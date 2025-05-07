package domain.usecases.task

import domain.repository.TaskRepository
import domain.utlis.TaskExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetLogsUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getLogsUseCase: GetTaskLogsUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        getLogsUseCase = GetTaskLogsUseCase(taskRepository)
        testScope = TestScope()
    }

    @Test
    fun `get TaskLogs should return list of logs when task repository return list of logs`() {
        //Given
        testScope.launch {
            val taskId = 1
            coEvery { taskRepository.getTaskLogsByID("", taskId.toString()) } returns listOf()

            assertDoesNotThrow { getLogsUseCase.getTaskLogs("", taskId.toString()) }

        }
    }

    @Test
    fun `get Task Logs should throw exception when project repository throw exception`() {
        //Given
        testScope.launch {
            val projectId = 1
            coEvery {
                taskRepository.getTaskLogsByID(
                    "",
                    projectId.toString()
                )
            } throws TaskExceptions.NoLogsFoundException()

            assertThrows<TaskExceptions.NoLogsFoundException> { getLogsUseCase.getTaskLogs("", projectId.toString()) }

        }
    }
}