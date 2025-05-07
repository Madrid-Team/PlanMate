package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import domain.models.project.Project
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

class GetProjectByIdUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `should return project when project exists`() {
        runTest {
            // Given
            val projectId = "1"
            val mockProject: Project = mockk()
            coEvery { projectRepository.getProjectById(projectId) } returns mockProject

            // When
            val result = getProjectByIdUseCase.invoke(projectId)

            // Then
            assertDoesNotThrow { getProjectByIdUseCase.invoke(projectId) }
            assertThat(result).isEqualTo(mockProject)
        }
    }

    @Test
    fun ` should throw exception when project does not exist`() {
        runTest {
            // Given
            val projectId = "999"
            coEvery { projectRepository.getProjectById(projectId) } throws PlanMateExceptions("Project not found")

            // When & Then
            assertThrows<PlanMateExceptions> {
                getProjectByIdUseCase.invoke(projectId)
            }
        }
    }

}