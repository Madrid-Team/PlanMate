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
import domain.utlis.TaskNotFoundException
import io.mockk.*
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
        every { fileCsvReader.readCsvFile() } returns listOf("task1")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto()
        every { taskCsvParser.parseTaskToString(task.toDto()) } returns csvRow
        every { fileCsvWriter.updateCsvFile(any()) } returns Unit

        val result = taskDataSource.deleteTask(taskId.toString())

        assertThat(result).isTrue()
        verify { fileCsvWriter.updateCsvFile(any()) }
    }

    @Test
    fun `should delete task throw exception when task id is not found`() {
        assertThrows<TaskNotFoundException> {
            taskDataSource.deleteTask(task.id.toString())
        }
    }


    @Test
    fun `should create task return true when creating task successfully`() {
        val formattedLog = "User create Task at 2025/05/02 8:25 AM"
        val copiedTaskWithLog = task.copy(logs = listOf(formattedLog))

        every { taskCsvParser.parseTaskToString(copiedTaskWithLog.toDto()) } returns csvRow
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
        verify { taskCsvParser.parseTaskToString(copiedTaskWithLog.toDto()) }
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
        every { taskCsvParser.parseOneRowToTask("task") } returns task.toDto()
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto()

        val result = taskDataSource.getAllTasks()

        assertThat(result).isEqualTo(listOf(task, task))
    }

    @Test
    fun `getTaskByProjectId should return list of tasks when id is found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = projectId)
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = projectId)

        val result = taskDataSource.getTasksByProjectId(projectId)

        assertThat(result).containsExactly(task.copy(projectId = projectId), task.copy(projectId = projectId))
    }

    @Test
    fun `getTaskByProjectId should return empty list when id is not found`() {
        every { fileCsvReader.readCsvFile() } returns listOf("task1", "task2")
        every { taskCsvParser.parseOneRowToTask("task1") } returns task.toDto().copy(projectId = "id1")
        every { taskCsvParser.parseOneRowToTask("task2") } returns task.toDto().copy(projectId = "id2")

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