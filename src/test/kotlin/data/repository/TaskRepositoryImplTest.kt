package data.repository

import data.mapper.toDomain
import data.source.csv.task.TaskExternalDataSource
import data.source.csv.task.TaskManager
import data.source.csv.task.helperTaskDto
import domain.repository.TaskRepository
import domain.utils.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TaskRepositoryImplTest {
    private lateinit var taskExternalDataSource: TaskExternalDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskManager: TaskManager

    @BeforeEach
    fun setup() {
        taskExternalDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskExternalDataSource)
        taskManager = mockk(relaxed = true)
    }

    private val taskDto = helperTaskDto(id = "26fb5810-951e-4913-aae8-1d36d72d85eb", title = "task")

    @Test
    fun `editTask should pass edit task successfully to data source`() {
        runTest {
            assertDoesNotThrow { taskExternalDataSource.editTask(taskDto) }
        }
    }

    @Test
    fun `editTask should throw exception when failed to edit task`() {
        runTest {
            coEvery { taskManager.editTask(taskDto) } returns listOf(taskDto)
            coEvery { taskExternalDataSource.editTask(taskDto) } throws TaskCannotEditException("Failed to edit task")

            assertThrows<TaskCannotEditException> {
                taskRepository.editTask(taskDto.toDomain())
            }
        }
    }

    @Test
    fun `getTasksByProjectId should return list of taskDto data source return success`() {
        runTest {
            assertDoesNotThrow { taskExternalDataSource.getTasksByProjectId(taskDto.projectId) }
        }
    }

    @Test
    fun `getTaskByProjectID should throw exception tasks return from data source is empty`() {
        coEvery { taskExternalDataSource.getTasksByProjectId(taskDto.projectId) } throws TaskNotFoundException(
            "Failed to get tasks by project id"
        )
        runTest {
            assertThrows< TaskNotFoundException> { taskRepository.getTasksByProjectId(taskDto.projectId) }
        }
    }

    @Test
    fun `createTask should execute successfully when data source create task successfully`() {
        runTest {
            assertDoesNotThrow { taskRepository.createTask(taskDto.toDomain()) }
        }
    }

    @Test
    fun `createTask should throw exception when data source throw exception`() {
        runTest {
            coEvery { taskExternalDataSource.createTask(taskDto) } throws TaskExceptions("Create task failed")

            assertThrows<TaskExceptions> { taskRepository.createTask(taskDto.toDomain()) }
        }
    }


    @Test
    fun `deleteTask should execute successfully when data source delete task successfully`() {
        runTest {
            coEvery { taskExternalDataSource.deleteTask(taskId = taskDto.id) } returns Unit

            assertDoesNotThrow { taskRepository.deleteTaskByTaskId(taskDto.projectId) }
        }
    }

    @Test
    fun `deleteTask should throw exception when data source fails to delete`() {
        runTest {
            coEvery { taskExternalDataSource.deleteTask(taskId = taskDto.id) } throws TaskCannotDeleteException(
                "Failed to delete task"
            )
            assertThrows<TaskCannotDeleteException> { taskRepository.deleteTaskByTaskId(taskDto.id) }
        }
    }

    @Test
    fun `getTaskLogsById returns task's logs when it exists in tasks list`() {
        runTest {
            assertDoesNotThrow { taskRepository.getTaskLogsByTaskId(taskDto.projectId) }
        }
    }


    @Test
    fun `getTaskLogsByID throw Task not found exception when task  does not exist in tasks list`() {
        runTest {
            coEvery { taskExternalDataSource.getTaskLogsByID(taskId = taskDto.id) } throws NoLogsFoundException(
                "Failed to get task logs"
            )
            assertThrows<NoLogsFoundException> { taskRepository.getTaskLogsByTaskId(taskDto.id) }
        }
    }
}