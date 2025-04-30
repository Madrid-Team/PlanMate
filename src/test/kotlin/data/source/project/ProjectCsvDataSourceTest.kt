package data.source.project

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.usecases.createProject
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `createProject should return success when writing succeeds`() {
        //Given
        val project = createProject( name = "Test Project", description = "Desc")

        //When
        val result = dataSource.createProject(project)

        //Then
        assertTrue { result.isSuccess }
    }

}