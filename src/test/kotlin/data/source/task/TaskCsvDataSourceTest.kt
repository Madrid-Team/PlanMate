package data.source.task

import com.google.common.truth.Truth.assertThat
import data.utils.FileCsvWriter
import domain.usecases.task.createTask
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val task = createTask(id = "12", title = "task")

        val result = taskCsvDataSource.editTask(task)

        assertThat(result).isEqualTo(true)
    }
}