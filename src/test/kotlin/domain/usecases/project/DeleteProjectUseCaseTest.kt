package domain.usecases.project

import domain.repository.ProjectRepository
import domain.utils.ProjectExceptions
import io.mockk.coEvery
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
    fun `should delete project when it exists`() {
        runTest {
            //Given
            val projectId = "project-123"
            coEvery { getProjectByIdUseCase.getById(projectId) } returns mockk()
            coEvery { projectRepository.deleteProject(projectId) } returns Unit

            //When
            assertDoesNotThrow {
                deleteProjectUseCase.deleteProject(projectId)
            }
        }
    }

    @Test
    fun `should throw exception when project does not exist`() {
        runTest {
            //Given
            val projectId = "not-found"
            coEvery { getProjectByIdUseCase.getById(projectId) } throws ProjectExceptions.ProjectNotFoundException()

            //When
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                deleteProjectUseCase.deleteProject(projectId)
            }
        }
    }
}