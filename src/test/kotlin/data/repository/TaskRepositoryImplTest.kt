package data.repository

import com.google.common.truth.Truth
import data.source.task.TaskDataSource
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.createTask
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository


    @BeforeEach
    fun setup() {
        taskDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `get all task should return list of tasks when data source is not empty`() {
        val tasks = listOf(createTask(), createTask())
        every { taskDataSource.getAllTasks() } returns tasks

        val result = taskRepository.getAllTasks()

        Truth.assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `get all task should return empty list when data source is empty`() {
        val tasks = emptyList<Task>()
        every { taskDataSource.getAllTasks() } returns tasks

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
    fun `create task should return true when data source return true`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.createTask(task) } returns true

        val result = taskRepository.createTask(task)

        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `create task should throw exception when data source throw exception`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.createTask(task) } throws Exception()

        assertThrows<Exception> {
            taskRepository.createTask(task)
        }
    }


    @Test
    fun `delete task should return true when data source return true`() {
        val taskId = "1313"
        every { taskDataSource.deleteTask(taskId) } returns true

        val result = taskRepository.deleteTask(taskId)

        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `delete task should throw exception when data source throw exception`() {
        val taskId = "1313"
        every { taskDataSource.deleteTask(taskId) } throws Exception()

        assertThrows<Exception> {
            taskRepository.deleteTask(taskId)
        }
    }

    @Test
    fun `edit task should return true when data source return true`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.editTask(task) } returns true

        val result = taskRepository.editTask(task)

        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `edit task should throw exception when data source throw exception`() {
        val task = createTask(id = "1231", title = "task")
        every { taskDataSource.editTask(task) } throws Exception()

        assertThrows<Exception> {
            taskRepository.editTask(task)
        }
    }
}