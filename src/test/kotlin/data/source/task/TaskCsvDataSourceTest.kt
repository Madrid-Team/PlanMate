package data.source.task

import com.google.common.truth.Truth.assertThat
import data.dto.task.TaskDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.*

class TaskCsvDataSourceTest {
    private lateinit var taskCsvParser: TaskCsvParser
    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var taskCsvDataSource: ExternalTaskDataSource
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var taskManager: TaskManager

    @BeforeEach
    fun setup() {
        taskCsvParser = mockk(relaxed = true)
        fileCsvWriter = mockk(relaxed = true)
        fileCsvReader = mockk(relaxed = true)
        taskManager = mockk(relaxed = true)
        taskCsvDataSource = TaskCsvDataSource(taskCsvParser, fileCsvWriter, fileCsvReader, taskManager)
    }

    @Test
    fun `editTask should update file successfully`() {
        runTest {
            val task = helperTaskDto(id = UUID.randomUUID().toString(), title = "task")
            val updatedTask = helperTaskDto(id = UUID.randomUUID().toString(), title = "task")
            every { taskManager.getTasks() } returns listOf(task)
            every { taskManager.editTask(task) } returns listOf(task)
            every { taskCsvParser.parseTaskToString(task) } returns csvRow
            every { fileCsvWriter.updateCsvFile(any()) } returns Unit


            assertDoesNotThrow { taskCsvDataSource.editTask(updatedTask) }
        }
    }


    @Test
    fun `editTask should throw exception when edit csv file throw exception`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(csvRow) } throws IOException()

        assertThrows<Exception> {
            fileCsvWriter.updateCsvFile(csvRow)
        }
    }

    @Test
    fun `deleteTask should execute successfully when csv delete successfully`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        assertDoesNotThrow { taskManager.deleteTask(tasksDto.id) }
    }


    @Test
    fun `createTask should execute successfully when creating task successfully`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit

        assertDoesNotThrow { taskManager.addTask(task) }
    }

    @Test
    fun `createTask should throw exception when writing to csv file is failed`() {
        runTest {
            every { taskCsvParser.parseTaskToString(task) } returns csvRow
            every { fileCsvWriter.writeToCsvFile(any()) } throws IOException()
            every { taskManager.addTask(task) } returns Unit

            assertThrows<IOException> { taskCsvDataSource.createTask(task) }
        }
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when list of tasks is not empty`() {
        runTest {
            val tasksDto = listOf(helperTaskDto(projectId = projectId))
            every { taskManager.getTasks() } returns tasksDto

            val result = taskCsvDataSource.getTasksByProjectId(projectId)

            assertThat(result).isEqualTo(tasksDto)
        }
    }

    @Test
    fun `getTaskByProjectId should return empty list when list of tasks is empty`() {
        runTest {
            val emptyTasks = emptyList<TaskDto>()
            every { taskManager.getTasks() } returns emptyList()

            val result = taskCsvDataSource.getTasksByProjectId("12")

            assertThat(result).isEqualTo(emptyTasks)
        }
    }

    companion object {
        val tasks = listOf("task1", "task2")
        val task = helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")
        const val csvRow = "08a823be-fc8f-4310-9bcb-543d577ebb93,task"
        const val projectId = "08a823be-fc8f-4310-9bcb-543d577ebb93"
        val tasksDto = helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")

    }
}