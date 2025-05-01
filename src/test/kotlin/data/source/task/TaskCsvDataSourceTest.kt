package data.source.task

import com.google.common.truth.Truth.assertThat
import data.utils.FileCsvWriter
import domain.usecases.task.createTask
import domain.utlis.CannotCreateTaskException
import domain.utlis.TaskCannotEditException
import domain.utlis.TaskNotFoundException
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TaskCsvDataSourceTest {
    private lateinit var taskCsvParser: TaskCsvParser
    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var taskCsvDataSource: TaskCsvDataSource

    @BeforeEach
    fun setup() {
        taskCsvParser = mockk()
        fileCsvWriter = mockk()
        taskCsvDataSource = TaskCsvDataSource(taskCsvParser, fileCsvWriter)
    }

    @Test
    fun `should edit task return true when edit task successfully`() {

        val result = taskCsvDataSource.editTask(task)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should edit task throw exception when failed to edit the task`() {
        assertThrows<TaskCannotEditException> {
            taskCsvDataSource.editTask(task)
        }
    }

    @Test
    fun `should delete task return true when task id is found`() {
        val result = taskCsvDataSource.deleteTask(task.id)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should delete task throw exception when task id is not found`() {
        assertThrows<TaskNotFoundException> {
            taskCsvDataSource.deleteTask(task.id)
        }
    }


    @Test
    fun `should create task return true when creating task successfully`() {
        val result = taskCsvDataSource.createTask(task)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should create task throw exception when creating task is failed`() {
        assertThrows<CannotCreateTaskException> {
            taskCsvDataSource.createTask(task)
        }
    }


    companion object {
        val task = createTask(id = "12", title = "task")
    }
}