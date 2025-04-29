package domain.usecases

import domain.repository.ProjectRepository
import domain.utlis.ProjectNotFoundException
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `deleteProject should return a success result when project deleted successfully`() {
        //Given
        val projectId = 1

        //When
        val result = deleteProjectUseCase.deleteProject(projectId.toString())

        //Then
        assertTrue { result }

    }

    @Test
    fun `deleteProject should throw ProjectNotFoundException when project not exists`() {
        //Given
        val projectId = 1

        //When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.deleteProject(projectId.toString())
        }


    }

}