package domain.usecases.task

import com.google.common.truth.Truth.assertThat
import domain.repository.ProjectRepository
import domain.repository.TaskRepository
import domain.usecases.createProject
import domain.utils.ProjectExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

//class DisplayAllTasksUseCaseTest {
//    private lateinit var projectRepository: ProjectRepository
//    private lateinit var taskRepository: TaskRepository
//    private lateinit var displayAllTasksUseCase: DisplayAllTasksUseCase
//    private lateinit var testScope: TestScope
//
//    @BeforeEach
//    fun setUp() {
//        projectRepository = mockk()
//        taskRepository = mockk()
//        displayAllTasksUseCase = DisplayAllTasksUseCase(projectRepository, taskRepository)
//        testScope = TestScope()
//    }
//
//    @Test
//    fun `display should return Project not found message when project does not exist`() {
//        // Given
//        testScope.runTest {
//            val projectId = UUID.randomUUID().toString()
//            coEvery { projectRepository.getProjectById(projectId) } throws ProjectExceptions.ProjectNotFoundException()
//
//            assertThrows<ProjectExceptions.ProjectNotFoundException> { displayAllTasksUseCase.display(projectId) }
//        }
//    }
//
//    @Test
//    fun `display should return empty swimlanes when no tasks exist`() {
//        testScope.runTest {
//            // Given
//            val projectId = UUID.randomUUID().toString()
//            val project = createProject(
//                id = projectId,
//                name = "Empty Project",
//                taskStates = listOf("TODO", "In Progress", "Done")
//            )
//            val tasks = listOf(
//                createTask(projectId = projectId, title = "task1"),
//                createTask(projectId = projectId, title = "task2")
//            )
//
//            coEvery { projectRepository.getProjectById(projectId) } returns project
//            coEvery { taskRepository.getTasksByProjectId(projectId) } returns tasks
//
//            // When
//            val result = displayAllTasksUseCase.display(projectId)
//            // Then
//            coVerify { projectRepository.getProjectById(projectId) }
//            coVerify() { taskRepository.getTasksByProjectId(projectId) }
//            val expectedOutput = """""".trim()
//            assertThat(result).isEqualTo(expectedOutput)
//        }
//    }
//
//    @Test
//    fun `display should return formatted swimlanes when project and tasks exist`() {
//        testScope.runTest {
//            // Given
//            val projectId = UUID.randomUUID().toString()
//            val project = createProject(
//                id = projectId,
//                name = "My Project",
//                taskStates = listOf("TODO", "In Progress", "Done")
//            )
//
//            val tasks = listOf(
//                createTask(id = UUID.randomUUID().toString(), title = "Task 1", state = "TODO", projectId = projectId),
//                createTask(
//                    id = UUID.randomUUID().toString(),
//                    title = "Task 2",
//                    state = "In Progress",
//                    projectId = projectId
//                ),
//                createTask(id = UUID.randomUUID().toString(), title = "Task 3", state = "Done", projectId = projectId)
//            )
//
//            coEvery { projectRepository.getProjectById(projectId) } returns project
//            coEvery { taskRepository.getTasksByProjectId(projectId) } returns tasks
//
//            // When
//            val result = displayAllTasksUseCase.display(projectId)
//
//            // Then
//            coVerify { projectRepository.getProjectById(projectId) }
//            coEvery { taskRepository.getTasksByProjectId(projectId) }
//            val expectedOutput = """
//            TODO:
//              - Task 1
//
//            In Progress:
//              - Task 2
//
//            Done:
//              - Task 3
//            """.trimIndent()
//            assertThat(result).isEqualTo(expectedOutput)
//        }
//    }
//
//
//}