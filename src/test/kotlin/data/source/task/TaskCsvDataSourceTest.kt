package data.source.task

import com.google.common.truth.Truth.assertThat
import data.dto.project.ProjectDto
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.taskHeader
import domain.models.logs.CreatedLogFormatter
import domain.models.logs.EntityType
import domain.usecases.task.createTask
import domain.utlis.TaskExceptions
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.UUID
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
    fun `should return success result when tasks are edited successfully`() {
        val tasks = listOf(
            helperTaskDto(title = "task1", description = "description one"),
            helperTaskDto(title = "task2", description = "description two")
        )
        val parsedTasks = listOf(
            "task1,description one\n",
            "task2,description two\n"
        )

        every { taskCsvParser.parseTaskToString(any()) } returnsMany parsedTasks
        every { fileCsvWriter.updateCsvFile(any()) } just runs


        val result = taskDataSource.editTask(tasks)


        assertThat(result.isSuccess).isTrue()
        verify { fileCsvWriter.updateCsvFile(String.taskHeader + parsedTasks.joinToString("")) }
    }


//    @Test
//    fun `should edit task throw exception when failed to edit the task`() {
//        assertThrows<TaskNotFoundException> {
//            taskDataSource.editTask(task)
//        }
//    }

    @Test
    fun `should delete task return true when task id is found`() {
        every { taskCsvParser.parseTaskToString(task.toDto()) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        val result = taskDataSource.deleteTask(tasksDto)

        assertTrue { result.isSuccess }
    }

//    @Test
//    fun `should delete task throw exception when task id is not found`() {
//        every { taskCsvParser.parseTaskToString(task.toDto()) } returns csvRow
//        every { fileCsvWriter.updateCsvFile(any()) } returns Unit
//
//        val result = taskDataSource.deleteTask(listOf(task.toDto()))
//
//        assertTrue { result.isFailure }
//    }


    @Test
    fun `should create task return true when creating task successfully`() {
        every { taskCsvParser.parseTaskToString(task.toDto()) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } returns Unit

        val result = taskDataSource.createTask(task.toDto())

        assertTrue { result.isSuccess }
    }

    @Test
    fun `should create task throw exception when creating task is failed`() {
        every { taskCsvParser.parseTaskToString(task.toDto()) } returns csvRow
        every { fileCsvWriter.writeToCsvFile(csvRow) } throws IOException()

        val result = taskDataSource.createTask(task.toDto())

        assertTrue { result.isFailure }
    }

    @Test
    fun `should get all tasks return list of tasks`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task", "task2")
        every { taskCsvParser.parseOneRowToTask("task") } returns task.toDto()
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto()

        val result = taskDataSource.getAllTasks()

        assertThat(result.getOrNull()).isEqualTo(listOf(task.toDto(), task.toDto()))
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when id is found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = projectId)
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = projectId)

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result)
    }

    @Test
    fun `getTaskByProjectId should return empty list when id is not found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = "id1")
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = "id2")

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result)
    }

    companion object {
        val task = createTask(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task")
        const val csvRow = "08a823be-fc8f-4310-9bcb-543d577ebb93,task"
        val taskId = task.id
        val projectId = "08a823be-fc8f-4310-9bcb-543d577ebb93"
        val tasksDto = listOf(
            helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "task"),
        )
//            helperTaskDto(id = "08a823be-fc8f-4310-9bcb-543d577ebb93", title = "test2")

    }
}