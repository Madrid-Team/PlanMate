package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.task.TaskDataSource
import data.source.task.TaskMemoryDataSource
import data.source.task.helperTaskDto
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.createTask
import domain.utlis.TaskExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.*
import kotlin.test.assertTrue

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskMemoryDataSource: TaskMemoryDataSource

    @BeforeEach
    fun setup() {
        taskDataSource = mockk(relaxed = true)
        taskMemoryDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskDataSource, taskMemoryDataSource)
    }

    @Test
    fun `editTask should pass edit task successfully to data source`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        val tasks = listOf(
            helperTaskDto(id = UUID.randomUUID().toString(), title = "task"),
            helperTaskDto(id = UUID.randomUUID().toString(), title = "task2")
        )

        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
        every { taskDataSource.editTask(tasks) } returns Unit

        assertDoesNotThrow { taskDataSource.editTask(tasks) }
    }

    @Test
    fun `editTask should throw exception when failed to edit task`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        val tasks = listOf(task)
        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
        every { taskDataSource.editTask(tasks.map { it.toDto() }) } throws IOException()

        assertThrows<Exception> {
            taskRepository.editTask(task)
        }
    }


    @Test
    fun `getAllTask should return list of tasks when data source is not empty`() {
        val tasks = listOf(createTask(), createTask())
        every { taskMemoryDataSource.getTasks() } returns tasks

        assertDoesNotThrow { taskRepository.getAllTasks() }
    }


    @Test
    fun `getAllTask should throw exception when data source throw exception`() {
        every { taskMemoryDataSource.getTasks() } returns listOf()
        assertThrows<TaskExceptions.TaskNotFoundException> { taskRepository.getAllTasks() }
    }


    @Test
    fun `getTasksByProjectId should return list of taskDto data source return success`() {
        val projectId = UUID.randomUUID().toString()
        every { taskDataSource.getAllTasks() } returns listOf()

        assertDoesNotThrow { taskDataSource.getTasksByProjectId(projectId) }
    }

    @Test
    fun `createTask should execute successfully when data source create task successfully`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        every { taskDataSource.createTask(task.toDto()) } returns Unit

        assertDoesNotThrow { taskRepository.createTask(task) }
    }

    @Test
    fun `createTask should throw exception when data source throw exception`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        every { taskDataSource.createTask(task.toDto()) } throws IOException()

        assertThrows<Exception> { taskRepository.createTask(task) }
    }


    @Test
    fun `deleteTask should execute successfully when data source delete task successfully`() {
        val taskId = UUID.randomUUID().toString()
        every { taskDataSource.deleteTask(tasks) } returns Unit

        assertDoesNotThrow { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `deleteTask should throw exception when data source fails to delete`() {
        val taskId = UUID.randomUUID().toString()
        val tasks = emptyList<Task>()
        every { taskMemoryDataSource.deleteTask(taskId) } returns tasks
        every { taskDataSource.deleteTask(tasks.map { it.toDto() }) } throws IOException()


        assertThrows<TaskExceptions> { taskRepository.deleteTask(taskId) }
    }

    @Test
    fun `getTaskLogsById returns task's logs when it exists in tasks list`() {

        val id = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = id, "20", logs = listOf("ahmed added a task", "ahmed deleted a task"))
        )
        every { taskMemoryDataSource.getTasks() } returns tasks.map { it.toDomain() }

        val result = taskRepository.getTaskLogsByID(id)


        assertTrue(result.isSuccess)
    }


    @Test
    fun `getTaskLogsByID throw Task not found exception when task  does not exist in tasks list`() {
        val id = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = id, "20", logs = listOf("ahmed added a task", "ahmed deleted a task"))
        )
        every { taskMemoryDataSource.getTasks() } returns tasks.map { it.toDomain() }

        val result = taskRepository.getTaskLogsByID("5")


        assertTrue(result.isFailure)
    }

    companion object {
        val tasks = listOf(
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test"),
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test2")
        )
    }
}