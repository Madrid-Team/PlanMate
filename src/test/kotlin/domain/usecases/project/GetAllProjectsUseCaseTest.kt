package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utlis.ProjectExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class GetAllProjectsUseCaseTest {

    lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }


    @Test
    fun `getAllProjects should return a success result when there are projects`() {
        val project = listOf(
            createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            ),
            createProject(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "dia"
            )
        )
        every { projectRepository.getAllProjects() } returns Result.success(project)
        val result = getAllProjectsUseCase.getAllProjects()
        assertTrue { result.isSuccess }
    }



    @Test
    fun `getAllProjects should return a fail result when there are no projects`() {
        every { projectRepository.getAllProjects() } returns Result.failure(ProjectExceptions(""))
        val result = getAllProjectsUseCase.getAllProjects()
        assertTrue { result.isFailure }
    }



}