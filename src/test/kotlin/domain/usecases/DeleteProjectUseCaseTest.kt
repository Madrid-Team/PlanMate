package domain.usecases

import domain.repository.ProjectRepository
import domain.usecases.project.DeleteProjectUseCase
import domain.utlis.PlanMateExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        every { projectRepository.deleteProject(projectId.toString()) } returns Result.success(Unit)

        //When
        val result = deleteProjectUseCase.deleteProject(projectId.toString())

        //Then
        assertTrue { result.isSuccess }

    }

    @Test
    fun `deleteProject should return a failure result when project not deleted successfully`() {
        //Given
        val projectId = 1
        every { projectRepository.deleteProject(projectId.toString()) } returns Result.failure(PlanMateExceptions(""))

        //When
        val result = deleteProjectUseCase.deleteProject(projectId.toString())

        //Then
        assertFalse { result.isSuccess }


    }

}