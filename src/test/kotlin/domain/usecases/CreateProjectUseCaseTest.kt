package domain.usecases

import data.createProject
import domain.mapper.toDomain
import domain.repository.ProjectRepository
import domain.usecases.project.CreateProjectUseCase
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
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
        val project =data.createProject(
            name = "Test Project",
            description = "project description",
            createdBy = "user2",
            projectState = "Todo",
            taskStates = listOf("Todo", "In progress"),
            projectStates = listOf("Testing","Todo"),
        )

        every { projectRepository.createProject(project) } returns Result.success(Unit)
        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        //Then
        assertTrue { result.isSuccess }

    }

    @Test
    fun `createProject should throw ProjectNameExistException when project name already exists`() {
        //Given
        val project = createProject(
            name = "Test Project",
        )

        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        // Then
        assertFalse { result.isSuccess }

    }

    @Test
    fun `createProject should throw ProjectNameInvalidException when project name is invalid string`() {
        //Given
        val project = createProject(
            name = "123 #$",
        )

        //When
        val result = createProjectUseCase.createProject(project.toDomain())
        // Then
        assertFalse { result.isSuccess }
    }

    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is invalid string`() {
        //Given
        val project = createProject(
            name = "Test Project",
            description = "123 #$"
        )

        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        //Then
        assertFalse{result.isSuccess}
    }


    @Test
    fun `createProject should throw ProjectDescriptionInvalidException when project description is empty`() {
        //Given
        val project = createProject(
            name = "Test Project",
            description = ""
        )
        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        //Then
        assertFalse{result.isSuccess}
    }

    @Test
    fun `createProject should throw ProjectStatesInvalidException when project states list is empty`() {
        //Given
        val project = createProject(
            name = "Test Project",
            projectStates = emptyList()
        )

        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        //Then
        assertFalse{result.isSuccess}
    }


    @Test
    fun `createProject should throw ProjectStatesInvalidException when task states list is empty`() {
        //Given
        val project = createProject(
            name = "Test Project",
            taskStates = emptyList()
        )

        //When
        val result = createProjectUseCase.createProject(project.toDomain())

        //Then
        assertFalse{result.isSuccess}
    }

}