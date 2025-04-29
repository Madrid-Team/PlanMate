package domain.usecases

import domain.repository.ProjectRepository
import domain.utlis.ProjectDescriptionInvalidException
import domain.utlis.ProjectNameExistException
import domain.utlis.ProjectNameInvalidException
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class CreateProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `createProject should return true when project added successfully`() {
        //Given
        val project = createProject(
            name = "Test Project",
        )

        //When
        val result = createProjectUseCase.createProject(project)

        //Then
        assertTrue { result }

    }

    @Test
    fun `createProject should throw ProjectNameExistException when project name already exists`() {
        //Given
        val project = createProject(
            name = "Test Project",
        )

        //When & Then
        assertThrows<ProjectNameExistException> {
            createProjectUseCase.createProject(project)
        }

    }

    @Test
    fun `createProject should throw ProjectNameInvalidException when project name is invalid string`() {
        //Given
        val project = createProject(
            name = "123 #$",
        )

        //When & Then
        assertThrows<ProjectNameInvalidException> {
            createProjectUseCase.createProject(project)
        }
    }

    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is invalid string`() {
        //Given
        val project = createProject(
            name = "Test Project",
            description = ""
        )

        //When & Then
        assertThrows<ProjectDescriptionInvalidException> {
            createProjectUseCase.createProject(project)
        }
    }


    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is empty`() {
        //Given
        val project = createProject(
            name = "Test Project",
            description = ""
        )

        //When & Then
        assertThrows<ProjectDescriptionInvalidException> {
            createProjectUseCase.createProject(project)
        }
    }



}