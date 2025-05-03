package data.source.task

import com.google.common.truth.Truth.assertThat
import data.mapper.toDto
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
import org.checkerframework.checker.units.qual.t
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

        val result = taskDataSource.editTask(taskList)

        assertThat(result.isSuccess).isTrue()
        verify { fileCsvWriter.updateCsvFile(csvRow) }
    }

    @Test
    fun `should edit task throw exception when failed to edit the task`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.editTask(taskList)
        }
    }

    @Test
    fun `should delete task return true when task id is found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        val result = taskDataSource.deleteTask(taskList)

        assertThat(result.isSuccess).isTrue()
        verify { fileCsvWriter.updateCsvFile(any()) }
    }

    @Test
    fun `should delete task throw exception when task id is not found`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.deleteTask(taskList)
        }
    }


    @Test
    fun `should create task return true when creating task successfully`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit

        val result = taskDataSource.createTask(task)

        assertThat(result.isSuccess).isTrue()
        verify { taskCsvParser.parseTaskToString(task) }
        verify { fileCsvWriter.writeToCsvFile(csvRow) }
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

//    @Test
//    fun `getTaskByProjectId should return list of tasks when id is found`() {
//        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
//        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = projectId)
//        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = projectId)
//
//        val result = taskDataSource.getTasksByProjectId(projectId)
//
//        assertThat(result).containsExactly(task.copy(projectId = projectId), task.copy(projectId = projectId))
//    }

//    @Test
//    fun `getTaskByProjectId should return empty list when id is not found`() {
//        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
//        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = "id1")
//        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = "id2")
//
//        val result = taskDataSource.getTasksByProjectId(projectId)
//
//        assertThat(result).isEmpty()
//    }

    companion object {
        val task = createTask(id = "12", title = "task")
        const val csvRow = "12,task"
        val taskId = task.id
        val projectId = "12"
        val taskList = listOf(
            createTask(id = "12", title = "task"),
            createTask(id = "13", title = "task"),
            createTask(id = "14", title = "task"),
        )
    }

}