package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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
        every { projectRepository.deleteProject(projectId.toString()) } returns Unit

        //When
        assertDoesNotThrow {
            deleteProjectUseCase.deleteProject(projectId.toString())
        }


    }

    @Test
    fun `deleteProject should return a failure result when project not deleted successfully`() {
        //Given
        val projectId = 1
        every { projectRepository.deleteProject(projectId.toString()) } throws PlanMateExceptions("")

        //When
        assertThrows<PlanMateExceptions> {
            deleteProjectUseCase.deleteProject(projectId.toString())
        }
    }

}