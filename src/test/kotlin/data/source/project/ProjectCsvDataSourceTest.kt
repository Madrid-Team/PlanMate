//package data.source.project
//
//import data.createProject
//import data.utils.FileCsvReader
//import data.utils.FileCsvWriter
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertDoesNotThrow
//import org.junit.jupiter.api.assertThrows
//import java.io.IOException
//
//class ProjectCsvDataSourceTest {
//    private lateinit var fileCsvWriter: FileCsvWriter
//    private lateinit var fileCsvReader: FileCsvReader
//    private lateinit var projectCsvParser: ProjectCsvParser
//    private lateinit var dataSource: ProjectCsvDataSource
//
//    @BeforeEach
//    fun setUp() {
//        fileCsvWriter = mockk(relaxed = true)
//        fileCsvReader = mockk(relaxed = true)
//        projectCsvParser = mockk(relaxed = true)
//        dataSource = ProjectCsvDataSource(fileCsvReader, fileCsvWriter, projectCsvParser, mockk())
//    }
//
//    @Test
//    fun `createProject should return success result when writing succeeds`() {
//        //Given
//        val project = createProject(name = "Test Project", description = "Desc")
//
//        //When & Then
//        assertDoesNotThrow {
//            dataSource.createProject(project)
//        }
//    }
//
//    @Test
//    fun `createProject should return exception when writing throws exception`() {
//        //Given
//        every { fileCsvWriter.writeToCsvFile(any()) } throws IOException()
//        val project = createProject(name = "Test Project", description = "Desc")
//
//        //When & Then
//        assertThrows<IOException> {
//            dataSource.createProject(project)
//        }
//    }
//
//    @Test
//    fun `editProject should return success result when writing succeeds`() {
//        //Given
//        val project = listOf(createProject(name = "Test Project", description = "Desc"))
//        every { fileCsvWriter.writeToCsvFile(any()) } returns Unit
//        every { projectCsvParser.parseProjectToString(project[0]) } returns ""
//        //When & Then
//        assertDoesNotThrow {
//            dataSource.editProject(project)
//        }
//
//    }
//
//    @Test
//    fun `editProject should return exception when writing throws exception`() {
//        //Given
//        val project = listOf(createProject(name = "Test Project", description = "Desc"))
//        every { projectCsvParser.parseProjectToString(project[0]) } returns ""
//        every { fileCsvWriter.updateCsvFile(any()) } throws IOException()
//
//        //When & Then
//        assertThrows<IOException> {
//            dataSource.editProject(project)
//        }
//    }
//
//}