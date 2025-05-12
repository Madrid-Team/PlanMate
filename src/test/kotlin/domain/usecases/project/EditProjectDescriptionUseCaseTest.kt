package domain.usecases.project

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.repository.ProjectRepository
import domain.usecases.createProject
import domain.usecases.logs.CreateLogUseCase
import domain.utils.ProjectExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectDescriptionUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var editProjectDescriptionUseCase: EditProjectDescriptionUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        editProjectDescriptionUseCase = EditProjectDescriptionUseCase(
            projectRepository,
            createLogUseCase,
            getProjectByIdUseCase
        )
    }

    @Test
    fun `should update project description when validation passes`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalDescription = "Original description"
        val newDescription = "Updated description"
        val logEntry = "User test updated project description"

        val originalProject = createProject(
            id = projectId.toString(),
            name = "Project Name",
            description = originalDescription,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = originalProject.name,
                entityType = EntityType.PROJECT,
                username = "test",
                fieldName = "description",
                oldValue = originalDescription,
                newValue = newDescription,
                any()
            )
        } returns logEntry
        coEvery { projectRepository.editProject(any()) } just runs

        // When
        editProjectDescriptionUseCase.execute(projectId, newDescription)

        // Then
        coVerify {
            projectRepository.editProject(match {
                it.description == newDescription &&
                        it.projectLogs.contains(logEntry)
            })
        }
    }

    @Test
    fun `should throw exception when description is unchanged`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalDescription = "Original description"

        val originalProject = createProject(
            id = projectId.toString(),
            name = "Project Name",
            description = originalDescription,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject

        // When & Then
        assertThrows<ProjectExceptions.NoChangesException> {
            editProjectDescriptionUseCase.execute(projectId, originalDescription)
        }

        // Verify no updates were made
        coVerify(exactly = 0) { projectRepository.editProject(any()) }
        verify(exactly = 0) { createLogUseCase.invoke(any(), any(), any(), any(), any(), any(), any()) }
    }
}