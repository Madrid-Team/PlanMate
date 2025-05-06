package domain.usecases.project

import domain.models.project.Project
import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
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
        // Given
        val mockProjects = listOf<Project>(mockk(), mockk())
        every { projectRepository.getAllProjects() } returns mockProjects

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertDoesNotThrow { getAllProjectsUseCase.getAllProjects() }
        assertEquals(mockProjects, result)
    }

    @Test
    fun `should throw exception when repository fails`() {
        // Given
        every { projectRepository.getAllProjects() } throws PlanMateExceptions("Failed to fetch projects")

        // When & Then
        assertThrows<PlanMateExceptions> {
            getAllProjectsUseCase.getAllProjects()
        }
    }
}