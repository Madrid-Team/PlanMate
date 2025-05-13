package domain.usecases.project

import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var projectValidator: ProjectValidator
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        projectValidator = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository, projectValidator, getProjectByIdUseCase)
    }

    @Test
    fun `should edit project when all validations pass`() = runTest {
        //Given
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "Project description"
        )
        coEvery { getProjectByIdUseCase.getById(any()) } returns project
        coEvery { projectRepository.editProject(any()) } returns Unit

        //When & Then
        assertDoesNotThrow {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project does not exist`() = runTest {
        //Given
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "Project description"
        )
        coEvery { getProjectByIdUseCase.getById(any()) } throws ProjectExceptions.ProjectNotFoundException()

        //When & Then
        assertThrows<ProjectExceptions.ProjectNotFoundException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project name is invalid`() = runTest {
        //Given
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Invalid Name",
            description = "Project description"
        )
        coEvery { getProjectByIdUseCase.getById(any()) } throws ProjectExceptions.ProjectNameInvalidException()
        coEvery { projectValidator.validateName(any()) } throws ProjectExceptions.ProjectNameInvalidException()

        //When & Then
        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project description is empty`() = runTest {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Valid Name",
            description = ""
        )
        coEvery { projectValidator.validate(any()) } throws ProjectExceptions.ProjectDescriptionIsEmptyException()

        assertThrows<ProjectExceptions.ProjectDescriptionIsEmptyException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project description is invalid`() = runTest {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Valid Name",
            description = "###Invalid###"
        )
        coEvery { projectValidator.validate(any()) } throws ProjectExceptions.ProjectDescriptionInvalidException()

        assertThrows<ProjectExceptions.ProjectDescriptionInvalidException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project description is too short`() = runTest {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Valid Name",
            description = "Short"
        )
        coEvery { projectValidator.validate(any()) } throws ProjectExceptions.ProjectDescriptionTooShortException()

        assertThrows<ProjectExceptions.ProjectDescriptionTooShortException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project states is empty`() = runTest {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Valid Name",
            description = "Valid Description"
        )
        coEvery { projectValidator.validate(any()) } throws ProjectExceptions.ProjectStatesIsEmptyException()

        assertThrows<ProjectExceptions.ProjectStatesIsEmptyException> {
            editProjectUseCase.editProject(project)
        }
    }

    @Test
    fun `should throw exception when project task states is empty`() = runTest {
        val project = createProject(
            id = UUID.randomUUID().toString(),
            name = "Valid Name",
            description = "Valid Description"
        )
        coEvery { projectValidator.validate(any()) } throws ProjectExceptions.ProjectTaskStatesIsEmptyException()

        assertThrows<ProjectExceptions.ProjectTaskStatesIsEmptyException> {
            editProjectUseCase.editProject(project)
        }
    }

}