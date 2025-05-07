package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class GetProjectLogsByIdUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectLogsByIdUseCase: GetProjectLogsByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectLogsByIdUseCase = GetProjectLogsByIdUseCase(projectRepository)
    }

    @Test
    fun `getProjectLogs should return true when project repository called and return success result`() {
        runTest {
            //Given
            val projectId = 1
            coEvery { projectRepository.getProjectLogsById(projectId.toString()) } returns listOf()

            //When
            assertDoesNotThrow {
                getProjectLogsByIdUseCase.getProjectLogsById(projectId.toString())
            }
        }
    }

    @Test
    fun `getProjectLogs should return false when project repository called and return failure result`() {
        runTest {
            //Given
            val projectId = 1
            coEvery { projectRepository.getProjectLogsById(projectId.toString()) } throws PlanMateExceptions("")

            //When
            assertThrows<PlanMateExceptions> {

                getProjectLogsByIdUseCase.getProjectLogsById(projectId.toString())
            }
        }
    }

}