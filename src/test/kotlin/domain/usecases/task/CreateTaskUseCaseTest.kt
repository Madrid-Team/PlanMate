package domain.usecases.task

import domain.repository.TaskRepository
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.ProjectExceptions
import domain.utils.TaskExceptions
import domain.utils.TaskExceptions.TaskTitleIsEmptyException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskValidator: TaskValidator
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        taskValidator = mockk(relaxed = true)
        getProjectByIdUseCase = mockk(relaxed = true)
        createTaskUseCase = CreateTaskUseCase(
            taskRepository,
            getProjectByIdUseCase,
            taskValidator
        )
        testScope = TestScope()
    }

    @Test
    fun `should create task successfully when data is valid`() {
        testScope.runTest {
            //Given
            val task = createTask(title = "new task", description = "description")
            // When & Then
            assertDoesNotThrow {
                createTaskUseCase.createTask(task)
            }
            coVerify { getProjectByIdUseCase.getById(task.projectId) }
            coVerify { taskValidator.validateAll(task) }
            coVerify { taskRepository.createTask(task) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project ID is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(projectId = "")
            coEvery { getProjectByIdUseCase.getById(task.projectId) } throws
                    ProjectExceptions.ProjectNotFoundException(
                        "Project ID cannot be empty"
                    )

            // When & Then
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                createTaskUseCase.createTask(task)
            }

            coVerify(exactly = 0) { taskRepository.createTask(any()) }
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when project cannot be found by ID`() {
        testScope.runTest {
            // Given
            val task = createTask(projectId = "invalid-project-id")
            coEvery { getProjectByIdUseCase.getById(task.projectId) } throws
                    ProjectExceptions.ProjectNotFoundException(
                        "Project not found"
                    )

            // When & Then
            assertThrows<ProjectExceptions.ProjectNotFoundException> {
                createTaskUseCase.createTask(task)
            }
            coVerify(exactly = 0) { taskValidator.validateAll(any()) }
            coVerify(exactly = 0) { taskRepository.createTask(any()) }
        }

    }

    @Test
    fun `should throw TaskTitleIsEmptyException when task title is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(title = "")
            coEvery { taskValidator.validateAll(task) } throws TaskTitleIsEmptyException()

            // When & Then
            assertThrows<TaskTitleIsEmptyException> {
                createTaskUseCase.createTask(task)
            }

            coVerify(exactly = 0) { taskRepository.createTask(any()) }
        }
    }
    @Test
    fun `should throw TaskDescriptionIsEmptyException when description is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(description = "")
            coEvery { taskValidator.validateAll(task) } throws TaskExceptions.TaskDescriptionIsEmptyException()

            // When & Then
            assertThrows<TaskExceptions.TaskDescriptionIsEmptyException> {
                createTaskUseCase.createTask(task)
            }

            coVerify(exactly = 0) { taskRepository.createTask(any()) }
        }
    }

    @Test
    fun `should throw TaskStateIsEmptyException when task state is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(state = "")
            coEvery { taskValidator.validateAll(task) } throws TaskExceptions.TaskStateIsEmptyException()

            // When & Then
            assertThrows<TaskExceptions.TaskStateIsEmptyException> {
                createTaskUseCase.createTask(task)
            }

            coVerify(exactly = 0) { taskRepository.createTask(any()) }
        }
    }


}