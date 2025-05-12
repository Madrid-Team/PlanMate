package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class GetAllProjectsUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `should return projects list when repository returns successfully`() {
        runTest {
            // Given
            val mockProjects = listOf<Project>(mockk(), mockk())
            coEvery { projectRepository.getAllProjects() } returns mockProjects

            // When
            val result = getAllProjectsUseCase.execute()

            // Then
            assertDoesNotThrow { getAllProjectsUseCase.execute() }
            assertEquals(mockProjects, result)
        }
    }

    @Test
    fun `should throw exception when repository fails`() {
        runTest {
            // Given
            coEvery { projectRepository.getAllProjects() } throws ProjectExceptions("Failed to fetch projects")

            // When & Then
            assertThrows<ProjectExceptions> {
                getAllProjectsUseCase.execute()
            }
        }
    }
}