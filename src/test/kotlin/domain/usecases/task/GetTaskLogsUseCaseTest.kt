package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

//class GetTaskLogsUseCaseTest {
//    private lateinit var taskRepository: TaskRepository
//    private lateinit var getTaskLogsUseCase: GetTaskLogsUseCase
//    private lateinit var testScope: TestScope
//
//    @BeforeEach
//    fun setup() {
//        taskRepository = mockk(relaxed = true)
//        getTaskLogsUseCase = GetTaskLogsUseCase(taskRepository)
//        testScope = TestScope()
//    }
//
//    @Test
//    fun `get TaskLogs should return list of logs when task repository return list of logs`() {
//        //Given
//        testScope.runTest {
//            val taskId = 1
//            coEvery { taskRepository.getTaskLogsByID("", taskId.toString()) } returns listOf()
//
//            assertDoesNotThrow { getTaskLogsUseCase("", taskId.toString()) }
//
//        }
//    }
//
//    @Test
//    fun `get Task Logs should throw exception when project repository throw exception`() {
//        //Given
//        testScope.runTest {
//            val projectId = 1
//            coEvery {
//                taskRepository.getTaskLogsByID(
//                    "",
//                    projectId.toString()
//                )
//            } throws TaskExceptions.NoLogsFoundException()
//
//            assertThrows<TaskExceptions.NoLogsFoundException> { getTaskLogsUseCase("", projectId.toString()) }
//
//        }
//    }
//}