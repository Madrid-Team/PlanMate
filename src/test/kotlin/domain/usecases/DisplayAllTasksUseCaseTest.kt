package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import domain.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DisplayAllTasksUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        taskRepository = mockk()
        displayAllTasksUseCase = DisplayAllTasksUseCase(projectRepository, taskRepository)
    }

    @Test
    fun `should return Project not found message when project does not exist`() {
        // Given
        val projectId = "non_existing_project"
        every { projectRepository.getProjectById(projectId) } returns null

        // When
        val result = displayAllTasksUseCase.display(projectId)

        // Then
        assertThat(result).isEqualTo("Project not found.")
    }

    @Test
    fun `should return empty swimlanes when no tasks exist`() {
        // Given
        val projectId = "p2"
        val project = createProject(
            id = projectId,
            name = "Empty Project",
            taskStates = listOf("TODO", "In Progress", "Done")
        )

        every { projectRepository.getProjectById(projectId) } returns project
        every { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        // When
        val result = displayAllTasksUseCase.display(projectId)
        // Then
        verify { projectRepository.getProjectById(projectId) }
        verify { taskRepository.getTasksByProjectId(projectId) }
        val expectedOutput = """
    TODO:

    In Progress:

    Done:
""".trimIndent()
        assertThat(result).isEqualTo(expectedOutput)
    }

}