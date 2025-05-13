package data.source.project

import data.createProject
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException

//class ProjectCsvDataSourceTest {
//    private lateinit var fileCsvWriter: FileCsvWriter
//    private lateinit var fileCsvReader: FileCsvReader
//    private lateinit var projectCsvParser: ProjectCsvParser
//    private lateinit var dataSource: ProjectCsvDataSource
//    private lateinit var testScope: TestScope
//    private lateinit var projectManager: ProjectManager
//
//
//    @BeforeEach
//    fun setUp() {
//        fileCsvWriter = mockk(relaxed = true)
//        fileCsvReader = mockk(relaxed = true)
//        projectCsvParser = mockk(relaxed = true)
//        projectManager = mockk(relaxed = true)
//        dataSource = ProjectCsvDataSource(fileCsvReader, fileCsvWriter, projectCsvParser, projectManager)
//        testScope = TestScope()
//    }
//
//    @Test
//    fun `createProject should return success result when writing succeeds`() {
//        testScope.launch {
//            //Given
//            val project = createProject(name = "Test Project", description = "Desc")
//            val row = "Test Project , Desc"
//            every { projectManager.getProjects() } returns emptyList()
//
//            every { projectCsvParser.parseProjectToString(project) } returns row
//            every { fileCsvWriter.writeToCsvFile(row) } returns Unit
//            every { projectManager.addProject(project) } returns Unit
//
//
//            //When & Then
//            assertDoesNotThrow {
//                dataSource.createProject(project)
//            }
//        }
//    }
//
//    @Test
//    fun `createProject should return exception when writing throws exception`() {
//        testScope.launch {
//            //Given
//            every { fileCsvWriter.writeToCsvFile(any()) } throws IOException()
//            val project = createProject(name = "Test Project", description = "Desc")
//
//            //When & Then
//            assertThrows<IOException> {
//                dataSource.createProject(project)
//            }
//        }
//    }
//
//    @Test
//    fun `editProject should return success result when writing succeeds`() {
//        testScope.launch {
//            //Given
//            val project = createProject(name = "Test Project", description = "Desc")
//            every { fileCsvWriter.writeToCsvFile(any()) } returns Unit
//            every { projectCsvParser.parseProjectToString(project) } returns ""
//            //When & Then
//            assertDoesNotThrow {
//                dataSource.editProject(project)
//            }
//        }
//    }
//
//    @Test
//    fun `editProject should return exception when writing throws exception`() {
//        testScope.launch {
//            //Given
//            val project = createProject(name = "Test Project", description = "Desc")
//            every { projectCsvParser.parseProjectToString(project) } returns ""
//            every { fileCsvWriter.updateCsvFile(any()) } throws IOException()
//
//            //When & Then
//            assertThrows<IOException> {
//                dataSource.editProject(project)
//            }
//        }
//    }
//}