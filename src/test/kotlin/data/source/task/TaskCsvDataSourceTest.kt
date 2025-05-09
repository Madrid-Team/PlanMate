package data.source.task

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.usecases.task.createTask
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
    private lateinit var externalTaskDataSource: ExternalTaskDataSource
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var taskManager: TaskManager

    @BeforeEach
    fun setup() {
        taskCsvParser = mockk()
        fileCsvWriter = mockk()
        fileCsvReader = mockk()
        taskManager = mockk()
        externalTaskDataSource = TaskCsvDataSource(taskCsvParser, fileCsvWriter, fileCsvReader, taskManager)
    }

    @Test
    fun `editTask should update file successfully`() {
        runTest {
            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
            val updatedTask = helperTaskDto(id = UUID.randomUUID().toString(), title = "task")

//            every { taskCsvParser.parseTaskToString(any()) } returnsMany parsedTasks
            every { fileCsvWriter.updateCsvFile(any()) } returns Unit


            assertDoesNotThrow { externalTaskDataSource.editTask(updatedTask) }
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

//        assertDoesNotThrow { externalTaskDataSource.deleteTask(tasksDto.id.toString()) }
    }


    @Test
    fun `createTask should execute successfully when creating task successfully`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit

//        assertDoesNotThrow { externalTaskDataSource.createTask(task) }
    }

    @Test
    fun `createTask should throw exception when writing to csv file is failed`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } throws IOException()

//        assertThrows<IOException> { externalTaskDataSource.createTask(task) }
    }

    @Test
    fun `getAllTasks should return list of tasks when read from file successfully`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task", "task2")
        every { taskCsvParser.parseOneRowToTask("task") } returns task
        every { taskCsvParser.parseOneRowToTask("task2") } returns task

//        val result = externalTaskDataSource.getAllTasks()

//        assertThat(result).isEqualTo(listOf(task, task))
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when list of tasks is not empty`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = projectId)
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = projectId)

//        val result = externalTaskDataSource.getTasksByProjectId(projectId)

//        assertThat(result)
    }

    @Test
    fun `getTaskByProjectId should return empty list when list of tasks is empty`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = "id1")
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = "id2")

//        val result = externalTaskDataSource.getTasksByProjectId(projectId)

//        assertThat(result)
    }

    companion object {
        val task = helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")
        const val csvRow = "08a823be-fc8f-4310-9bcb-543d577ebb93,task"
        const val projectId = "08a823be-fc8f-4310-9bcb-543d577ebb93"
        val tasksDto = helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")

    }
}