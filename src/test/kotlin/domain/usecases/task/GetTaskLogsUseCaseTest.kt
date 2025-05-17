package domain.usecases.task

import domain.models.logs.AuditLog
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.AuditLogRepository
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class GetTaskLogsUseCaseTest {
    private lateinit var getTaskLogsUseCase: GetTaskLogsUseCase
    private lateinit var testScope: TestScope
    private lateinit var auditLoRepository: AuditLogRepository

    @BeforeEach
    fun setup() {
        auditLoRepository = mockk(relaxed = true)
        getTaskLogsUseCase = GetTaskLogsUseCase(auditLoRepository)
        testScope = TestScope()
    }


    @Test
    fun `should return logs when task logs exist`() {
        testScope.runTest {
            // Given
            val taskId = "valid_task_id"
            val logs = listOf(
                AuditLog(
                    operationType = OperationType.UPDATE,
                    entityName = "",
                    entityType = EntityType.TASK,
                    entityId = "",
                    username = ""
                )
            )
            coEvery { auditLoRepository.getAuditLogForTaskById(taskId) } returns logs

            // When & Then
            assertDoesNotThrow {
                getTaskLogsUseCase.getTaskLogsByTaskId(taskId)
            }

            coVerify(exactly = 1) { auditLoRepository.getAuditLogForTaskById(taskId) }
        }
    }

    @Test
    fun `should throw NoLogsFoundException when no logs are found`() {
        testScope.runTest {
            // Given
            val taskId = "valid_task_id"
            coEvery { auditLoRepository.getAuditLogForTaskById(taskId) } returns emptyList()

            // When & Then
            assertThrows<TaskExceptions.NoLogsFoundException> {
                getTaskLogsUseCase.getTaskLogsByTaskId(taskId)
            }

            coVerify(exactly = 1) { auditLoRepository.getAuditLogForTaskById(taskId) }
        }
    }

}