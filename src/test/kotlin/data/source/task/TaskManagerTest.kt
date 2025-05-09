package data.source.task

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class TaskManagerTest {

    private lateinit var taskManager: TaskManager

    @BeforeEach
    fun setUp() {
        taskManager = TaskManager()
    }

    @Test
    fun `getTasks should return empty list initially`() {
        val tasks = taskManager.getTasks()
        assertThat(tasks).isEmpty()
    }


    @Test
    fun `getTasks should return list of tasks`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = taskId1),
            helperTaskDto(id = taskId2)
        )
        taskManager.setTasks(tasks)

        assertThat(taskManager.getTasks()).isNotEmpty()
        assertThat(taskManager.getTasks()).containsExactly(tasks[0], tasks[1])
    }

    @Test
    fun `setTasks should add multiple tasks`() {
        val task1 = helperTaskDto()
        val task2 = helperTaskDto()
        taskManager.setTasks(listOf(task1, task2))
        val result = taskManager.getTasks()
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `addTask should add single task`() {
        val task = helperTaskDto()
        org.junit.jupiter.api.assertDoesNotThrow {
            taskManager.addTask(task)
        }
    }

    @Test
    fun `deleteTask should remove the correct task`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = taskId1),
            helperTaskDto(id = taskId2)
        )
        taskManager.setTasks(tasks)
        val updatedTasks = taskManager.deleteTask(taskId1)
        assertThat(updatedTasks.size).isEqualTo(1)
    }

    @Test
    fun `editTask should update the correct task`() {
        val taskId1 = UUID.randomUUID().toString()
        val taskId2 = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = taskId1, title = "Old task"),
            helperTaskDto(id = taskId2)
        )
        taskManager.setTasks(tasks)

        val result = taskManager.editTask(tasks[0].copy(title = "Updated task"))
        assertThat(result.find { it.id.toString() == taskId1 }!!.title).isEqualTo("Updated task")
    }

    @Test
    fun `addTask should add a new task to the list`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTaskDto(id = taskId1, title = "Added task")
        taskManager.addTask(task)

        val result = taskManager.getTasks()
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id.toString()).isEqualTo(taskId1)
    }

    @Test
    fun `addTask should add a new task`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTaskDto(id = taskId1)
        taskManager.addTask(task)

        val tasks = taskManager.getTasks()
        assertThat( tasks.size).isEqualTo(1)
        assertThat(tasks[0].id.toString()).isEqualTo(taskId1)
    }

    @Test
    fun `deleteTask should remove a task by ID`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTaskDto(id = taskId1)

        taskManager.addTask(task)
        val updatedTasks = taskManager.deleteTask(taskId1)
        assertTrue { updatedTasks.isEmpty() }
    }

    @Test
    fun `editTask should update an existing task`() {
        val taskId1 = UUID.randomUUID().toString()
        val task = helperTaskDto(id = taskId1, title = "New task")
        taskManager.addTask(task)

        val result = taskManager.editTask(task.copy(title = "Updated task"))
        assertThat(result.find { it.id.toString() == taskId1 }?.title).isEqualTo("Updated task")
    }
}