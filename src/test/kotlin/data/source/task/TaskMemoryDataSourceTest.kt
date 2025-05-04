package data.source.task

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.feature.tasks.helperTask
import java.util.*
import kotlin.test.assertTrue

class TaskMemoryDataSourceTest {

    private lateinit var taskMemoryDataSource: TaskMemoryDataSource

    @BeforeEach
    fun setUp() {
        taskMemoryDataSource = TaskMemoryDataSource()
    }

    @Test
    fun `getTasks should return empty list initially`() {
        val tasks = taskMemoryDataSource.getTasks()
        assertThat(tasks).isEmpty()
    }


    @Test
    fun `getTasks should return list of tasks`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTask(id = taskId1),
            helperTask(id = taskId2)
        )
        taskMemoryDataSource.setTasks(tasks)

        assertThat(taskMemoryDataSource.getTasks()).isNotEmpty()
        assertThat(taskMemoryDataSource.getTasks()).containsExactly(tasks[0], tasks[1])
    }

    @Test
    fun `setTasks should add multiple tasks`() {
        val task1 = helperTask()
        val task2 = helperTask()
        taskMemoryDataSource.setTasks(listOf(task1, task2))
        val result = taskMemoryDataSource.getTasks()
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `addTask should add single task`() {
        val task = helperTask()
        org.junit.jupiter.api.assertDoesNotThrow {
            taskMemoryDataSource.addTask(task)
        }
    }

    @Test
    fun `deleteTask should remove the correct task`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTask(id = taskId1),
            helperTask(id = taskId2)
        )
        taskMemoryDataSource.setTasks(tasks)
        val updatedTasks = taskMemoryDataSource.deleteTask(taskId1)
        assertThat(updatedTasks.size).isEqualTo(1)
    }

    @Test
    fun `editTask should update the correct task`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTask(id = taskId1, title = "Old task"),
            helperTask(id = taskId2)
        )
        taskMemoryDataSource.setTasks(tasks)

        val result = taskMemoryDataSource.editTask(tasks[0].copy(title = "Updated task"))
        assertThat(result.find { it.id.toString() == taskId1 }!!.title).isEqualTo("Updated task")
    }

    @Test
    fun `addTask should add a new task to the list`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTask(id = taskId1, title = "Added task")
        taskMemoryDataSource.addTask(task)

        val result = taskMemoryDataSource.getTasks()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id.toString()).isEqualTo(taskId1)
    }

    @Test
    fun `addTask should add a new task`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTask(id = taskId1)
        taskMemoryDataSource.addTask(task)

        val tasks = taskMemoryDataSource.getTasks()
        assertThat( tasks.size).isEqualTo(1)
        assertThat(tasks[0].id.toString()).isEqualTo(taskId1)
    }

    @Test
    fun `deleteTask should remove a task by ID`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTask(id = taskId1)

        taskMemoryDataSource.addTask(task)
        val updatedTasks = taskMemoryDataSource.deleteTask(taskId1)
        assertTrue { updatedTasks.isEmpty() }
    }

    @Test
    fun `editTask should update an existing task`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTask(id = taskId1, title = "New task")
        taskMemoryDataSource.addTask(task)

        val result = taskMemoryDataSource.editTask(task.copy(title = "Updated task"))
        assertThat(result.find { it.id.toString() == taskId1 }?.title).isEqualTo("Updated task")
    }
}