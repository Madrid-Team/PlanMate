package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import domain.validation.ValidateProjectName
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectNameUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var validateProjectName: ValidateProjectName
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var editProjectNameUseCase: EditProjectNameUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        validateProjectName = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        editProjectNameUseCase = EditProjectNameUseCase(
            projectRepository,
            validateProjectName,
            createLogUseCase,
            getProjectByIdUseCase
        )
    }

    @Test
    fun `should update project name when validation passes`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalName = "Original Project"
        val newName = "Updated Project"
        val logEntry = "User test updated project name"

        val originalProject = createProject(
            id = projectId.toString(),
            name = originalName,
            description = "Project description",
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject
        every { validateProjectName(any()) } just runs
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = originalName,
                entityType = EntityType.PROJECT,
                username = "test",
                fieldName = "name",
                oldValue = originalName,
                newValue = newName,
                any()
            )
        } returns logEntry
        coEvery { projectRepository.editProject(any()) } just runs

        // When
        editProjectNameUseCase.execute(projectId, newName)

        // Then
        coVerify {
            projectRepository.editProject(match {
                it.name == newName &&
                        it.projectLogs.contains(logEntry)
            })
        }
    }

    @Test
    fun `should throw validation exception when project name is invalid`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalName = "Original Project"
        val invalidName = "Invalid@Name"

        val originalProject = createProject(
            id = projectId.toString(),
            name = originalName,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject
        every { validateProjectName(any()) } throws ProjectExceptions.ProjectNameInvalidException()

        // When & Then
        assertThrows<ProjectExceptions.ProjectNameInvalidException> {
            editProjectNameUseCase.execute(projectId, invalidName)
        }

        // Verify no updates were made
        coVerify(exactly = 0) { projectRepository.editProject(any()) }
    }

    @Test
    fun `should throw exception when name is unchanged`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalName = "Original Project"

        val originalProject = createProject(
            id = projectId.toString(),
            name = originalName,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject

        // When & Then
        assertThrows<ProjectExceptions.NoChangesException> {
            editProjectNameUseCase.execute(projectId, originalName)
        }

        // Verify no updates were made
        coVerify(exactly = 0) { projectRepository.editProject(any()) }
        verify(exactly = 0) { validateProjectName(any()) }
    }
}