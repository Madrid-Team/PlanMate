package data.source.task

import com.google.common.truth.Truth.assertThat
import data.dto.task.TaskDto
import data.mapper.toDto
import domain.usecases.task.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskCsvParserTest {
    private lateinit var task: TaskDto
    private lateinit var taskCsvParser: TaskCsvParser
    private val row = "1,2,title,description,state,created by,log1|log2|log3"

    @BeforeEach
    fun setup() {
        taskCsvParser = TaskCsvParser()
        task = taskCsvParser.parseOneRowToTask(row)
    }

    @Test
    fun `parseOneRowToTask function should parse project id correctly`() {
        assertThat(task.projectId).isEqualTo("2")
    }

    @Test
    fun `parseOneRowToTask function should parse task title correctly`() {
        assertThat(task.title).isEqualTo("title")
    }

    @Test
    fun `parseOneRowToTask function should parse task description correctly`() {
        assertThat(task.description).isEqualTo("description")
    }

    @Test
    fun `parseOneRowToTask function should parse task state correctly`() {
        assertThat(task.taskState).isEqualTo("state")
    }

    @Test
    fun `parseOneRowToTask function should parse task created by correctly`() {
        assertThat(task.createdBy).isEqualTo("created by")
    }

    @Test
    fun `parseOneRowToTask function should parse task log correctly`() {
        assertThat(task.logs).containsExactly("log1", "log2", "log3")
    }

    @Test
    fun `parseTaskToString function should parse task to string correctly`() {
        val task = createTask(
            id = UUID.randomUUID().toString(),
            projectId = "2",
            title = "title",
            description = "description",
            state = "state",
            createdBy = "created by",
            logs = listOf("log1", "log2", "log3")
        )

        val expectedValue = listOf(
            task.id,
            task.projectId,
            task.description,
            task.createdBy,
            task.taskState,
            task.title,
            task.logs.joinToString("|")
        ).joinToString(",")

        val result = taskCsvParser.parseTaskToString(task.toDto())
        assertThat(result).isEqualTo(expectedValue)
    }

}