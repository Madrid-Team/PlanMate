package data.repository

import com.google.common.truth.Truth.assertThat
import data.mapper.toDto
import data.source.task.TaskDataSource
import data.source.task.TaskMemoryDataSource
import data.source.task.helperTaskDto
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `editTask should return success result when data source return success`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        val tasks = listOf(
            helperTaskDto(id = UUID.randomUUID().toString(), title = "task"),
            helperTaskDto(id = UUID.randomUUID().toString(), title = "task2")
        )

        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
        every { taskDataSource.editTask(tasks) } returns Result.success(Unit)

        val result = taskRepository.editTask(task)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `editTask should return result failure when data source return failure`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        val tasks = listOf(task)
        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
        every { taskDataSource.editTask(tasks.map { it.toDto() }) } returns Result.failure(Exception())

        val result = taskRepository.editTask(task)

        assertTrue { result.isFailure }
        verify { taskMemoryDataSource.addTask(task) }
    }


    @Test
    fun `getAllTask should return list of tasks when data source is not empty`() {
        val tasks = listOf(createTask(), createTask())
        every { taskMemoryDataSource.getTasks() } returns tasks

        val result = taskRepository.getAllTasks()

        assertTrue { result.isSuccess }
    }


    @Test
    fun `getAllTask should return failure when data source return failure`() {
        every { taskMemoryDataSource.getTasks() } returns listOf()

        val result = taskRepository.getAllTasks()

        assertTrue { result.isFailure }
    }


    @Test
    fun `getTasksByProjectId should return list of taskDto data source return success`() {
        val projectId = UUID.randomUUID().toString()
        every { taskDataSource.getAllTasks() } returns Result.success(listOf())

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertTrue { result.isSuccess }
    }

    @Test
    fun `createTask should return success when data source return success`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        every { taskDataSource.createTask(task.toDto()) } returns Result.success(Unit)

        val result = taskRepository.createTask(task)

        assertTrue { result.isSuccess }
    }

    @Test
    fun `createTask should return failure when data source return failure`() {
        val task = createTask(id = UUID.randomUUID().toString(), title = "task")
        every { taskDataSource.createTask(task.toDto()) } returns Result.failure(Exception())

        val result = taskRepository.createTask(task)

        assertTrue { result.isFailure }
    }


    @Test
    fun `deleteTask should return success when data source return success`() {
        val taskId = UUID.randomUUID().toString()
        every { taskDataSource.deleteTask(tasks) } returns Result.success(Unit)

        val result = taskRepository.deleteTask(taskId)

        assertTrue { result.isSuccess }
    }

    @Test
    fun `deleteTask should return failure when data source return failure`() {
        val taskId = UUID.randomUUID().toString()
        val tasks = emptyList<Task>()
        every { taskMemoryDataSource.deleteTask(taskId) } returns tasks
        every { taskDataSource.deleteTask(tasks.map { it.toDto() }) } returns Result.failure(Exception())

        val result = taskRepository.deleteTask(taskId)

        assertTrue { result.isFailure }
    }

    companion object {
        val tasks = listOf(
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test"),
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test2")
        )
    }
}