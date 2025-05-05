package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        //Given
        val projectId = 1
        every { projectRepository.getProjectLogsById(projectId.toString()) } returns Result.success(listOf<String>())

        //When
        val result = getProjectLogsByIdUseCase.getProjectLogsById(projectId.toString())

        //Then
        kotlin.test.assertTrue { result.isSuccess }

    }

    @Test
    fun `getProjectLogs should return false when project repository called and return failure result`() {
        //Given
        val projectId = 1
        every { projectRepository.getProjectLogsById(projectId.toString()) } returns Result.failure(PlanMateExceptions(""))

        //When
        val result = getProjectLogsByIdUseCase.getProjectLogsById(projectId.toString())

        //Then
        kotlin.test.assertFalse { result.isSuccess }

    }
}