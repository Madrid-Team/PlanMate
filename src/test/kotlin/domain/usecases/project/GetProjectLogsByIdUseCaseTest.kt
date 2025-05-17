package domain.usecases.project

import data.dto.AuditLogDto
import domain.models.logs.AuditLog
import domain.repository.AuditLogRepository
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectLogsByIdUseCaseTest {
    private lateinit var auditLogRepository: AuditLogRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase

    @BeforeEach
    fun setUp() {
        auditLogRepository = mockk()
        getProjectByIdUseCase = mockk()
        getProjectLogsByIdUseCase = GetProjectLogsByIdUseCase(auditLogRepository, getProjectByIdUseCase)
    }

    @Test
    fun `should return project logs when project exists`() {
        runTest {
            //Given
            val projectId = "project-123"

            val log1 = mockk<AuditLog> {
                every { toString() } returns "Log 1"
            }
            val log2 = mockk<AuditLog> {
                every { toString() } returns "Log 2"
            }
            val auditLogs = listOf(log1, log2)
            val expectedLogs = listOf("Log 1", "Log 2")

            coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockk()
            coEvery { auditLogRepository.getAuditLogForProjectById(projectId)} returns auditLogs

            //When
            val result = getProjectLogsByIdUseCase.getProjectLogsByProjectId(projectId)

            //Then
            assert(result == expectedLogs)
        }
    }

    @Test
    fun `should throw exception when project does not exist`() {
        runTest {
            //Given
            val projectId = "not-found"
            coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws ProjectExceptions.ProjectNotFoundException()

            //When
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                getProjectLogsByIdUseCase.getProjectLogsByProjectId(projectId)
            }
        }
    }

    @Test
    fun `should throw NoLogsFoundException when project has no logs`() {
        runTest {
            // Given
            val projectId = "project-empty-logs"
            coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockk()
            coEvery { auditLogRepository.getAuditLogForProjectById(projectId) } returns emptyList()

            // When & Then
            assertThrows<ProjectExceptions.NoLogsFoundException> {
                getProjectLogsByIdUseCase.getProjectLogsByProjectId(projectId)
            }
        }
    }

}