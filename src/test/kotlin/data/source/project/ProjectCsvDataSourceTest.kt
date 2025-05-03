package data.source.project

import data.createProject
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import kotlin.test.assertTrue

class ProjectCsvDataSourceTest {
    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var projectCsvParser: ProjectCsvParser
    private lateinit var dataSource: ProjectCsvDataSource

    @BeforeEach
    fun setUp() {
        fileCsvWriter = mockk()
        fileCsvReader = mockk()
        projectCsvParser = mockk()
        dataSource = ProjectCsvDataSource(fileCsvReader, fileCsvWriter, projectCsvParser)
    }

    @Test
    fun `createProject should return success result when writing succeeds`() {
        //Given
        val project = createProject(name = "Test Project", description = "Desc")

        //When
        val result = dataSource.createProject(project)

        //Then
        assertTrue { result.isSuccess }
    }

    @Test
    fun `createProject should return exception when writing throws exception`() {
        //Given
        every { fileCsvWriter.writeToCsvFile(any()) } throws IOException()
        val project = createProject(name = "Test Project", description = "Desc")

        //When & Then
        assertThrows<IOException> {
            dataSource.createProject(project)
        }
    }

    @Test
    fun `editProject should return success result when writing succeeds`() {
        //Given
        val project = listOf(createProject(name = "Test Project", description = "Desc"))

        //When
        val result = dataSource.editProject(project)

        //Then
        assertTrue { result.isSuccess }
    }

    @Test
    fun `editProject should return exception when writing throws exception`() {
        //Given
        every { fileCsvWriter.writeToCsvFile(any()) } throws IOException()
        val project = listOf(createProject(name = "Test Project", description = "Desc"))

        //When & Then
        assertThrows<IOException> {
            dataSource.editProject(project)
        }
    }

}