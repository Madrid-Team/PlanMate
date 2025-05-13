package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectLogsByIdUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectByIdUseCase = mockk()
        getProjectLogsByIdUseCase = GetProjectLogsByIdUseCase(projectRepository, getProjectByIdUseCase)
    }

    @Test
    fun `should return project logs when project exists`() {
        runTest {
            //Given
            val projectId = "project-123"
            val expectedLogs = listOf("Log 1", "Log 2")

            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { projectRepository.getProjectLogsById(projectId) } returns expectedLogs

            //When
            val result = getProjectLogsByIdUseCase.execute(projectId)

            //Then
            assert(result == expectedLogs)
        }
    }

    @Test
    fun `should throw exception when project does not exist`() {
        runTest {
            //Given
            val projectId = "not-found"
            coEvery { getProjectByIdUseCase.getById(projectId) } throws ProjectExceptions.ProjectNotFoundException()

            //When
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                getProjectLogsByIdUseCase.execute(projectId)
            }
        }
    }

    @Test
    fun `should throw NoLogsFoundException when project has no logs`() {
        runTest {
            // Given
            val projectId = "project-empty-logs"
            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { projectRepository.getProjectLogsById(projectId) } returns emptyList()

            // When & Then
            assertThrows<ProjectExceptions.NoLogsFoundException> {
                getProjectLogsByIdUseCase.execute(projectId)
            }
        }
    }

}