package data.repository

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
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
import org.junit.jupiter.api.assertThrows

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository
    private val taskMemoryDataSource: TaskMemoryDataSource = mockk()


    @BeforeEach
    fun setup() {
        taskDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskDataSource, taskMemoryDataSource)
    }

    @Test
    fun `edit task should return success result when data source return success`() {
        val task = createTask(id = "1231", title = "task")
        val tasks = listOf(
            createTask(id = "1231", title = "task"),
            createTask(id = "123123", title = "task2")
        )
        every { taskDataSource.getAllTasks() } returns Result.success(tasks.map { it.toDto() })
        taskRepository = TaskRepositoryImpl(taskDataSource, taskMemoryDataSource)

        val result = taskRepository.editTask(task)

        assertThat(result.isSuccess).isTrue()
    }

//    @Test
//    fun `edit task should throw exception when data source throw exception`() {
//        val task = createTask(id = "1231", title = "task")
//        every { taskDataSource.editTask(task) } throws Exception()
//
//        assertThrows<Exception> {
//            taskRepository.editTask(task)
//        }
//    }


    @Test
    fun `get all task should return list of tasks when data source is not empty`() {
        val tasks = listOf(createTask(), createTask())
        every { taskDataSource.getAllTasks() } returns Result.success(tasks.map { it.toDto() })

        val result = taskRepository.getAllTasks()

        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `get all task should return empty list when data source is empty`() {
        val tasks = emptyList<Task>()
        every { taskDataSource.getAllTasks() } returns Result.success(tasks.map { it.toDto() })

        val result = taskRepository.getAllTasks()

        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `get all task should throw exception when data source throw exception`() {
        every { taskDataSource.getAllTasks() } throws Exception()

        assertThrows<Exception> {
            taskRepository.getAllTasks()
        }
    }

    @Test
    fun `getTasksByProjectId should return list of tasks when date source is not empty`() {
        val tasks = listOf(createTask(), createTask())
        val projectId = tasks[0].projectId
        every { taskDataSource.getTasksByProjectId(projectId) } returns Result.success(tasks.map { it.toDto() })

        val result = taskRepository.getTasksByProjectId(projectId)

        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `getTasksByProjectId should return empty list when date source is empty`() {
        val tasks = emptyList<Task>()
        val projectId = "12"
        every { taskDataSource.getTasksByProjectId(projectId) } returns Result.success(tasks.map { it.toDto() })

        val result = taskRepository.getTasksByProjectId(projectId)

        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `getTasksByProjectId should throw exception when date source throw exception`() {
        val projectId = "12"
        every { taskDataSource.getTasksByProjectId(projectId) } throws TaskExceptions.TaskNotFoundException()

        assertThrows<TaskExceptions.TaskNotFoundException> {
            taskRepository.getTasksByProjectId(projectId)
        }
    }

    @Test
    fun `create task should return true when data source return true`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.createTask(task.toDto()) } returns Result.success(Unit)

        val result = taskRepository.createTask(task)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `create task should throw exception when data source throw exception`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.createTask(task.toDto()) } throws Exception()

        assertThrows<Exception> {
            taskRepository.createTask(task)
        }
    }


    @Test
    fun `delete task should return true when data source return true`() {
        val taskId = "1313"
        every { taskDataSource.deleteTask(tasks) } returns Result.success(Unit)

        val result = taskRepository.deleteTask(taskId)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `delete task should throw exception when data source throw exception`() {
        val taskId = "1313"
        every { taskDataSource.deleteTask(tasks) } throws Exception()

        assertThrows<Exception> {
            taskRepository.deleteTask(taskId)
        }
    }

    companion object {
        val tasks = listOf(
            helperTaskDto(id = "1", title = "test"),
            helperTaskDto(id = "2", title = "test2")
        )
    }
}