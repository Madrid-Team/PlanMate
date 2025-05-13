package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
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
            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { projectRepository.deleteProject(projectId) } returns Unit

            // When
            assertDoesNotThrow {
                deleteProjectUseCase.deleteProject(projectId)
            }

            // Then
            coVerify(exactly = 1) { getProjectByIdUseCase.getById(projectId) }
            coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project does not exist and not call delete`() {
        runTest {
            // Given
            val projectId = "not-found"
            coEvery { getProjectByIdUseCase.getById(projectId) } throws ProjectExceptions.ProjectNotFoundException()

            // When & Then
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                deleteProjectUseCase.deleteProject(projectId)
            }

            // Then
            coVerify(exactly = 1) { getProjectByIdUseCase.getById(projectId) }
            coVerify(exactly = 0) { projectRepository.deleteProject(any()) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when projectId is blank`() {
        runTest {
            // Given
            val blankId = ""
            coEvery { getProjectByIdUseCase.getById(blankId) } throws ProjectExceptions.ProjectNotFoundException("Project ID cannot be empty")

            // When & Then
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                deleteProjectUseCase.deleteProject(blankId)
            }

            coVerify(exactly = 1) { getProjectByIdUseCase.getById(blankId) }
            coVerify(exactly = 0) { projectRepository.deleteProject(any()) }
        }
    }

    @Test
    fun `should throw exception when deleteProject throws unexpected error`() {
        runTest {
            // Given
            val projectId = "existing-id"
            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { projectRepository.deleteProject(projectId) } throws RuntimeException("DB error")

            // When & Then
            assertThrows<RuntimeException> {
                deleteProjectUseCase.deleteProject(projectId)
            }

            coVerify(exactly = 1) { getProjectByIdUseCase.getById(projectId) }
            coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
        }
    }


}