package data.source.task

import com.google.common.truth.Truth.assertThat
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.logs.CreatedLogFormatter
import domain.models.logs.EntityType
import domain.usecases.task.createTask
import domain.utlis.TaskNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

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
        val existingTask = task
        val updatedTask = existingTask.copy(title = "new title")

        every { fileCsvReader.readCsvFile() } returns listOf("row1")
        every { taskCsvParser.parseOneRowToTask("row1") } returns existingTask
        every { taskCsvParser.parseTaskToString(updatedTask) } returns csvRow
        every { fileCsvWriter.updateCsvFile(csvRow) } returns Unit

        val result = taskDataSource.editTask(updatedTask)

        assertThat(result).isTrue()
        verify { fileCsvWriter.updateCsvFile(csvRow) }
    }

    @Test
    fun `should edit task throw exception when failed to edit the task`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.editTask(task)
        }
    }

    @Test
    fun `should delete task return true when task id is found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        val result = taskDataSource.deleteTask(taskId)

        assertThat(result).isTrue()
        verify { fileCsvWriter.updateCsvFile(any()) }
    }

    @Test
    fun `should delete task throw exception when task id is not found`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.deleteTask(task.id)
        }
    }


    @Test
    fun `should create task return true when creating task successfully`() {
        val formattedLog = "User create Task at 2025/05/02 8:25 AM"
        val copiedTaskWithLog = task.copy(logs = listOf(formattedLog))

        every { taskCsvParser.parseTaskToString(copiedTaskWithLog) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit
        mockkObject(CreatedLogFormatter)
        every {
            CreatedLogFormatter.format(
                entityName = task.title,
                entityType = EntityType.TASK,
                username = task.createdBy
            )
        } returns formattedLog

        val result = taskDataSource.createTask(task)

        assertThat(result).isTrue()
        verify { taskCsvParser.parseTaskToString(copiedTaskWithLog) }
        verify { fileCsvWriter.writeToCsvFile(csvRow) }
        verify {
            CreatedLogFormatter.format(
                entityName = task.title,
                entityType = EntityType.TASK,
                username = task.createdBy
            )
        }
    }

    @Test
    fun `should create task throw exception when creating task is failed`() {
        assertThrows<IOException> {
            taskDataSource.createTask(task)
        }
    }

    @Test
    fun `should get all tasks return list of tasks`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task", "task2")
        every { taskCsvParser.parseOneRowToTask("task") } returns task
        every { taskCsvParser.parseOneRowToTask("task2") } returns task

        val result = taskDataSource.getAllTasks()

        assertThat(result).isEqualTo(listOf(task, task))
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when id is found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = projectId)
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = projectId)

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result).containsExactly(task.copy(projectId = projectId), task.copy(projectId = projectId))
    }

    @Test
    fun `getTaskByProjectId should return empty list when id is not found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = "id1")
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = "id2")

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result).isEmpty()
    }

    companion object {
        val task = createTask(id = "12", title = "task")
        const val csvRow = "12,task"
        val taskId = task.id
        val projectId = "12"
    }
}