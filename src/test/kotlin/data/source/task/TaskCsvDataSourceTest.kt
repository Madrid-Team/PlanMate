package data.source.task

import com.google.common.truth.Truth.assertThat
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.usecases.task.createTask
import domain.utlis.CannotCreateTaskException
import domain.utlis.TaskCannotEditException
import domain.utlis.TaskNotFoundException
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.concurrent.timerTask

class TaskCsvDataSourceTest {
    private lateinit var taskCsvParser: TaskCsvParser
    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var fileCsvReader: FileCsvReader

    @BeforeEach
    fun setup() {
        taskCsvParser = mockk()
        fileCsvWriter = mockk()
        fileCsvReader = mockk()
        taskDataSource = TaskCsvDataSource(taskCsvParser, fileCsvWriter, fileCsvReader)
    }

    @Test
    fun `should edit task return true when edit task successfully`() {
        val existingTask = createTask(
            id = "121",
            title = "title",
        )
        val updatedTask = existingTask.copy(title = "new title")
        val tasksList = listOf(existingTask)
        val csvString = "121,p1"

        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseTaskToString(updatedTask) } returns csvString
        every { fileCsvWriter.updateCsvFile(csvString) } returns Unit

        val result = taskDataSource.editTask(updatedTask)

        assertThat(result).isTrue()
        verify { fileCsvWriter.updateCsvFile(csvString) }
    }

    @Test
    fun `should edit task throw exception when failed to edit the task`() {
        assertThrows<TaskCannotEditException> {
            taskDataSource.editTask(task)
        }
    }

    @Test
    fun `should delete task return true when task id is found`() {
        val result = taskDataSource.deleteTask(task.id)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should delete task throw exception when task id is not found`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.deleteTask(task.id)
        }
    }


    @Test
    fun `should create task return true when creating task successfully`() {
        val result = taskDataSource.createTask(task)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `should create task throw exception when creating task is failed`() {
        assertThrows<CannotCreateTaskException> {
            taskDataSource.createTask(task)
        }
    }

    @Test
    fun `should get all tasks return list of tasks`() {
        val tasks = listOf(task, task)

        assertThat(taskDataSource.getAllTasks()).isEqualTo(tasks)
    }


    companion object {
        val task = createTask(id = "12", title = "task")
    }
}