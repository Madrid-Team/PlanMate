package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.ProjectNotFoundException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DeleteProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectByIdUseCase = mockk()
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository, getProjectByIdUseCase)
    }

    @Test
    fun `should delete project when data is valid`() {
        runTest {
            // Given
            val projectId = "project-123"
            coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockk()
            coEvery { projectRepository.deleteProjectByProjectId(projectId) } returns Unit

            // When
            assertDoesNotThrow {
                deleteProjectUseCase.deleteProjectByProjectId(projectId)
            }

            // Then
            coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
            coVerify(exactly = 1) { projectRepository.deleteProjectByProjectId(projectId) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project does not exist and not call delete`() {
        runTest {
            // Given
            val projectId = "not-found"
            coEvery { getProjectByIdUseCase.getProjectById(projectId) } throws ProjectNotFoundException()

            // When & Then
            assertThrows<ProjectNotFoundException> {
                deleteProjectUseCase.deleteProjectByProjectId(projectId)
            }

            // Then
            coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
            coVerify(exactly = 0) { projectRepository.deleteProjectByProjectId(any()) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when projectId is blank`() {
        runTest {
            // Given
            val blankId = ""
            coEvery { getProjectByIdUseCase.getProjectById(blankId) } throws ProjectNotFoundException("Project ID cannot be empty")

            // When & Then
            assertThrows<ProjectNotFoundException> {
                deleteProjectUseCase.deleteProjectByProjectId(blankId)
            }

            coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(blankId) }
            coVerify(exactly = 0) { projectRepository.deleteProjectByProjectId(any()) }
        }
    }

    @Test
    fun `should throw exception when deleteProject throws unexpected error`() {
        runTest {
            // Given
            val projectId = "existing-id"
            coEvery { getProjectByIdUseCase.getProjectById(projectId) } returns mockk()
            coEvery { projectRepository.deleteProjectByProjectId(projectId) } throws RuntimeException("DB error")

            // When & Then
            assertThrows<RuntimeException> {
                deleteProjectUseCase.deleteProjectByProjectId(projectId)
            }

            coVerify(exactly = 1) { getProjectByIdUseCase.getProjectById(projectId) }
            coVerify(exactly = 1) { projectRepository.deleteProjectByProjectId(projectId) }
        }
    }


}