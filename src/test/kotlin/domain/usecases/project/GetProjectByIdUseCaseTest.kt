package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetProjectByIdUseCaseTest {

    lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }


    @Test
    fun `getAllProjects should return a success result when there are projects`() {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "dia"
        )
        every { projectRepository.getProjectById(UUID.randomUUID().toString()) } returns project
        val result = getProjectByIdUseCase(UUID.randomUUID().toString())
        assertTrue { result.isSuccess }
    }


    @Test
    fun `getAllProjects should return a fail result when there are no projects`() {
        val projectId = UUID.randomUUID().toString()
        every { projectRepository.getProjectById(projectId) } returns null
        val result = getProjectByIdUseCase(projectId)
        assertTrue { result.isFailure }
    }


}