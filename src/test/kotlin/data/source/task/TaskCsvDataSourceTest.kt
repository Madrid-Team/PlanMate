package data.source.task

import com.google.common.truth.Truth.assertThat
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import kotlin.test.assertTrue

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
    fun `editTask should update file successfully`() {
        val tasks = listOf(
            helperTaskDto(title = "task1", description = "description one"),
            helperTaskDto(title = "task2", description = "description two")
        )
        val parsedTasks = listOf(
            "task1,description one\n",
            "task2,description two\n"
        )

        every { taskCsvParser.parseTaskToString(any()) } returnsMany parsedTasks
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit


        assertDoesNotThrow { taskDataSource.editTask(tasks) }
    }


    @Test
    fun `editTask should throw exception when edit csv file throw excprion`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(csvRow) } throws IOException()

        assertThrows<Exception> {
            fileCsvWriter.updateCsvFile(csvRow)
        }
    }

    @Test
    fun `deleteTask should return failure when failed to update file`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        val result = taskDataSource.deleteTask(tasksDto)

        assertTrue { result.isSuccess }
    }


    @Test
    fun `createTask should execute successfully when creating task successfully`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit

        assertDoesNotThrow { taskDataSource.createTask(task) }
    }

    @Test
    fun `createTask should throw exception when writing to csv file is failed`() {
        every { taskCsvParser.parseTaskToString(task) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } throws IOException()

        assertThrows<IOException> { taskDataSource.createTask(task) }
    }

    @Test
    fun `getAllTasks should return list of tasks when read from file successfully`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task", "task2")
        every { taskCsvParser.parseOneRowToTask("task") } returns task
        every { taskCsvParser.parseOneRowToTask("task2") } returns task

        val result = taskDataSource.getAllTasks()

        assertThat(result.getOrNull()).isEqualTo(listOf(task, task))
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when list of tasks is not empty`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = projectId)
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = projectId)

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result)
    }

    @Test
    fun `getTaskByProjectId should return empty list when list of tasks is empty`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.copy(projectId = "id1")
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.copy(projectId = "id2")

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result)
    }

    companion object {
        val task = helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")
        const val csvRow = "08a823be-fc8f-4310-9bcb-543d577ebb93,task"
        const val projectId = "08a823be-fc8f-4310-9bcb-543d577ebb93"
        val tasksDto = listOf(
            helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task"),
        )
    }
}