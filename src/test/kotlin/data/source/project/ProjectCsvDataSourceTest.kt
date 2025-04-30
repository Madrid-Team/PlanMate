package data.source.project

import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

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

}