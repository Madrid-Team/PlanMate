package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.usecases.createProject
import domain.utlis.ProjectExceptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

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
    fun `display should return Project not found message when project does not exist`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        every { projectRepository.getProjectById(projectId) } throws ProjectExceptions.ProjectNotFoundException()

        // When
        val result = displayAllTasksUseCase.display(projectId)

        // Then
        assertThat(result.exceptionOrNull()!!.message.toString()).isEqualTo("Project not found.")
    }

    @Test
    fun `display should return empty swimlanes when no tasks exist`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val project = createProject(
            id = projectId,
            name = "Empty Project",
            taskStates = listOf("TODO", "In Progress", "Done")
        )

        every { projectRepository.getProjectById(projectId) } returns project
        every { taskRepository.getTasksByProjectId(projectId) } returns Result.success(emptyList())

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
        assertThat(result.getOrNull()).isEqualTo(expectedOutput)
    }

    @Test
    fun `display should return formatted swimlanes when project and tasks exist`() {
        // Given
        val projectId = UUID.randomUUID().toString()
        val project = createProject(
            id = projectId,
            name = "My Project",
            taskStates = listOf("TODO", "In Progress", "Done")
        )

        val tasks = listOf(
            createTask(id = UUID.randomUUID().toString(), title = "Task 1", state = "TODO", projectId = projectId),
            createTask(id = UUID.randomUUID().toString(), title = "Task 2", state = "In Progress", projectId = projectId),
            createTask(id = UUID.randomUUID().toString(), title = "Task 3", state = "Done", projectId = projectId)
        )

        every { projectRepository.getProjectById(projectId) } returns project
        every { taskRepository.getTasksByProjectId(projectId) } returns Result.success(tasks)

        // When
        val result = displayAllTasksUseCase.display(projectId)

        // Then
        verify { projectRepository.getProjectById(projectId) }
        verify { taskRepository.getTasksByProjectId(projectId) }
        val expectedOutput = """
    TODO:
    - Task 1

    In Progress:
    - Task 2

    Done:
    - Task 3
""".trimIndent()
        assertThat(result.getOrNull()).isEqualTo(expectedOutput)
    }


}