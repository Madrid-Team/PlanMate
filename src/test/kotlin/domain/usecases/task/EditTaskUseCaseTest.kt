package domain.usecases.task

import domain.repository.TaskRepository
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class EditTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var taskValidatorUseCase: TaskValidatorUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        taskValidatorUseCase = mockk(relaxed = true)
        editTaskUseCase = EditTaskUseCase(taskRepository,taskValidatorUseCase)
        testScope = TestScope()
    }


    @Test
    fun `should edit task successfully when data is valid`() {
        testScope.runTest {
            // Given
            val task = createTask(title = "Updated Task", description = "Updated description")
            coEvery { taskValidatorUseCase.validateBasic(task) } returns Unit
            coEvery { taskRepository.editTask(task) } returns Unit

            // When & Then
            assertDoesNotThrow {
                editTaskUseCase.editTask(task)
            }

            coVerify(exactly = 1) { taskValidatorUseCase.validateBasic(task) }
            coVerify(exactly = 1) { taskRepository.editTask(task) }
        }
    }
    @Test
    fun `should throw TaskTitleIsEmptyException when task title is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(title = "")
            coEvery { taskValidatorUseCase.validateBasic(task) } throws TaskExceptions.TaskTitleIsEmptyException()

            // When & Then
            assertThrows<TaskExceptions.TaskTitleIsEmptyException> {
                editTaskUseCase.editTask(task)
            }

            coVerify(exactly = 1) { taskValidatorUseCase.validateBasic(task) }
            coVerify(exactly = 0) { taskRepository.editTask(any()) }
        }
    }

    @Test
    fun `should throw TaskDescriptionIsEmptyException when task description is empty`() {
        testScope.runTest {
            // Given
            val task = createTask(description = "")
            coEvery { taskValidatorUseCase.validateBasic(task) } throws TaskExceptions.TaskDescriptionIsEmptyException()

            // When & Then
            assertThrows<TaskExceptions.TaskDescriptionIsEmptyException> {
                editTaskUseCase.editTask(task)
            }

            coVerify(exactly = 1) { taskValidatorUseCase.validateBasic(task) }
            coVerify(exactly = 0) { taskRepository.editTask(any()) }
        }
    }

    @Test
    fun `should throw TaskEditFailedException when repository fails to edit task`() {
        testScope.runTest {
            // Given
            val task = createTask(title = "Some title", description = "Some description")
            val expectedException = TaskExceptions.TaskCannotEditException("Failed to edit task")

            coEvery { taskValidatorUseCase.validateBasic(task) } returns Unit
            coEvery { taskRepository.editTask(task) } throws expectedException

            // When & Then
            val thrown = assertThrows<TaskExceptions.TaskCannotEditException> {
                editTaskUseCase.editTask(task)
            }

            assert(thrown.message == "Failed to edit task")

            coVerify(exactly = 1) { taskValidatorUseCase.validateBasic(task) }
            coVerify(exactly = 1) { taskRepository.editTask(task) }
        }
    }



}
