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

class EditProjectStateUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createLogUseCase: CreateLogUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var editProjectStateUseCase: EditProjectStateUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        createLogUseCase = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        editProjectStateUseCase = EditProjectStateUseCase(
            projectRepository,
            createLogUseCase,
            getProjectByIdUseCase
        )
    }

    @Test
    fun `should update project state when validation passes`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalState = "TODO"
        val newState = "IN_PROGRESS"
        val logEntry = "User test updated project state"

        val originalProject = createProject(
            id = projectId.toString(),
            name = "Project Name",
            projectState = originalState,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject
        every {
            createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = originalProject.name,
                entityType = EntityType.PROJECT,
                username = "test",
                fieldName = "project state",
                oldValue = originalState,
                newValue = newState,
                any()
            )
        } returns logEntry
        coEvery { projectRepository.editProject(any()) } just runs

        // When
        editProjectStateUseCase.execute(projectId, newState)

        // Then
        coVerify {
            projectRepository.editProject(match {
                it.projectState == newState &&
                        it.projectLogs.contains(logEntry)
            })
        }
    }

    @Test
    fun `should throw exception when state is unchanged`() = runTest {
        // Given
        val projectId = UUID.randomUUID()
        val originalState = "TODO"

        val originalProject = createProject(
            id = projectId.toString(),
            name = "Project Name",
            projectState = originalState,
            createdBy = "test"
        )

        coEvery { getProjectByIdUseCase.getById(projectId.toString()) } returns originalProject

        // When & Then
        assertThrows<ProjectExceptions.NoChangesException> {
            editProjectStateUseCase.execute(projectId, originalState)
        }

        // Verify no updates were made
        coVerify(exactly = 0) { projectRepository.editProject(any()) }
        verify(exactly = 0) { createLogUseCase.invoke(any(), any(), any(), any(), any(), any(), any()) }
    }
}