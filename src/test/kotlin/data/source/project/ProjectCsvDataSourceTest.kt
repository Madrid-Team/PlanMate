package data.source.project

import data.createProject
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.project.Project
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectCsvDataSourceTest {
    private lateinit var fileCsvWriter: FileCsvWriter
    private lateinit var fileCsvReader: FileCsvReader
    private lateinit var projectCsvParser: ProjectCsvParser
    private lateinit var dataSource: ProjectCsvDataSource

    @BeforeEach
    fun setUp() {
        fileCsvWriter = mockk(relaxed = true)
        fileCsvReader = mockk(relaxed = true)
        projectCsvParser = mockk(relaxed = true)
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
        val result = dataSource.createProject(project)
        assertTrue { result.isFailure }
    }

    @Test
    fun `editProject should return success result when writing succeeds`() {
        //Given
        val project = listOf(createProject(name = "Test Project", description = "Desc"))
        every { fileCsvWriter.writeToCsvFile(any()) } returns Unit
        every { projectCsvParser.parseProjectToString(project[0]) } returns ""
        //When
        val result = dataSource.editProject(project)

        //Then
        assertTrue { result.isSuccess }
    }

    @Test
    fun `editProject should return exception when writing throws exception`() {
        //Given
        val project = listOf(createProject(name = "Test Project", description = "Desc"))
        every { projectCsvParser.parseProjectToString(project[0]) } returns ""
        every { fileCsvWriter.updateCsvFile(any()) } throws IOException()

        //When & Then
        val result = dataSource.editProject(project)
       assertTrue { result.isFailure }
    }
    @Test
    fun `deleteProject should return success when projects are deleted correctly`() {
        // Arrange
        val projectDto1 =  Project(UUID.randomUUID(), name = "Test Project 1", description = "This is a test project.", projectState = "OPEN", createdBy = "admin",
            projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED")
            , projectStates = listOf("OPEN", "CLOSED")).toDto()
        val projectDto2 = Project(UUID.randomUUID(), name = "Test Project 2", description = "This is a test project.", projectState = "OPEN", createdBy = "admin", projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED"), projectStates = listOf("OPEN", "CLOSED")).toDto()

        val projects = listOf(projectDto1, projectDto2)
        val projectAsString = "Project 1, Description 1"

        every { projectCsvParser.parseProjectToString(projectDto1) } returns projectAsString
        every { projectCsvParser.parseProjectToString(projectDto2) } returns projectAsString
        every { fileCsvWriter.updateCsvFile(any()) } just Runs  // No-op for updating the file

        // Act
        val result = dataSource.deleteProject(projects)

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteProject should return failure when an exception occurs during deletion`() {
        // Arrange
        val projectDto1 =  Project(UUID.randomUUID(), name = "Test Project 1", description = "This is a test project.", projectState = "OPEN", createdBy = "admin",
            projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED")
            , projectStates = listOf("OPEN", "CLOSED")).toDto()
        val projectDto2 = Project(UUID.randomUUID(), name = "Test Project 2", description = "This is a test project.", projectState = "OPEN", createdBy = "admin", projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED"), projectStates = listOf("OPEN", "CLOSED")).toDto()

        val projects = listOf(projectDto1, projectDto2)

        every { projectCsvParser.parseProjectToString(projectDto1) } returns "Project 1, Description 1"
        every { projectCsvParser.parseProjectToString(projectDto2) } returns "Project 2, Description 2"
        every { fileCsvWriter.updateCsvFile(any()) } throws Exception("File update error")

        // Act
        val result = dataSource.deleteProject(projects)

        // Assert
        assertTrue(result.isFailure)
        assertEquals("File update error", result.exceptionOrNull()?.message)
    }
    @Test
    fun `getProjects should return success when projects are read correctly`() {
        // Arrange
        val projectRow = listOf("Project Name", "Project Description").toString()
        val projectDto = Project(UUID.randomUUID(), name = "Test Project 2", description = "This is a test project.", projectState = "OPEN", createdBy = "admin", projectLogs = listOf("created", "updated"), taskStates = listOf("OPEN", "CLOSED"), projectStates = listOf("OPEN", "CLOSED")).toDto()

        every { fileCsvReader.readCsvFile() } returns listOf(projectRow)
        every { projectCsvParser.parseOneRowToProject(projectRow) } returns projectDto

        // Act
        val result = dataSource.getProjects()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Test Project 2", result.getOrNull()?.first()?.name)
    }

    @Test
    fun `getProjects should return failure when an exception occurs`() {
        // Arrange
        every { fileCsvReader.readCsvFile() } throws Exception("File read error")

        // Act
        val result = dataSource.getProjects()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("File read error", result.exceptionOrNull()?.message)
    }

}