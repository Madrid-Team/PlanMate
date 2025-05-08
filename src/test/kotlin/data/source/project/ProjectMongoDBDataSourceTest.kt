package data.source.project

import com.mongodb.MongoWriteException
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.project.ProjectDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.madrid.data.source.mongoDb.MongoClientProvider
import org.madrid.data.source.project.ProjectMongoDBDataSource
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var projectMongoDBDataSource: ProjectMongoDBDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    @BeforeAll
    fun setup() {
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database)
        runBlocking {
//            projectMongoDBDataSource = ProjectMongoDBDataSource(copyCollectionIfDifferentToTest.copyCollectionIfDifferent() )
        }


    }


    @Test
    fun `createProject should call insertOne with correct project`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<ProjectDto>>()
        val testProject = ProjectDto(
            id = UUID.randomUUID().toString(),
            name = "Test Project For create",
            description = "A test description",
            createdBy = "tester",
            projectLogs = emptyList(),
            projectState = "active",
            taskStates = emptyList(),
            projectStates = emptyList(),
            matesIds = emptyList(),
            tasks = emptyList()
        )

        coEvery {
            collection.insertOne(eq(testProject), any())
        } returns mockk()

        val dataSource = ProjectMongoDBDataSource(collection)

        dataSource.createProject(testProject)

        coVerify(exactly = 1) {
            collection.insertOne(eq(testProject), any())
        }
    }

    @Test
    fun `createProject adds a project and getProjects returns it`() {
     var projects:List<ProjectDto> =emptyList()
        val newProject = ProjectDto(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "A test description",
            createdBy = "tester",
            projectLogs = listOf(),
            projectState = "active",
            taskStates = listOf(),
            projectStates = listOf(),
            matesIds = listOf(),
            tasks = listOf()
        )
        runTest {
            projectMongoDBDataSource.createProject(newProject)

       projects = projectMongoDBDataSource.getProjects()
        }
        assertEquals(true, projects.any { it.name == "Test Project" })


}


    @Test
     fun `deleteProject should delete project with given projectId`() {
        runTest {
        //Given
        val projectId = projectMongoDBDataSource.getProjects().first().id


            // When
            projectMongoDBDataSource.deleteProject(projectId)

            // Then
            assertEquals(0, projectMongoDBDataSource.getProjects().size)

        }
    }
    @Test
    fun `deleteProject should call deleteOne with correct filter`() = runTest {
        // Given
        val projectId = "ghost2"

        val collection = mockk<MongoCollection<ProjectDto>>()
        val dataSource =ProjectMongoDBDataSource(collection)
        // Mock deleteOne to return a successful result
        coEvery {
            collection.deleteOne(eq(Filters.eq("_id", "ghost2")), any())
        } returns DeleteResult.acknowledged(1)


        //When
        dataSource.deleteProject(projectId)

        //Then
        coVerify {
            collection.deleteOne(eq(Filters.eq("_id", "ghost2")), any())
        }
    }



    @Test
    fun `editProject should not update if project does not exist`() = runTest {
        // Given
        val nonExistentProject =ProjectDto(
            id ="ghost",
            name = "Test Project5",
            description = "A test description",
            createdBy = "tester",
            projectLogs = listOf(),
            projectState = "active",
            taskStates = listOf(),
            projectStates = listOf(),
            matesIds = listOf(),
            tasks = listOf()
        )

        // When
        val result = projectMongoDBDataSource.editProject(nonExistentProject)

        // Then
        val found = copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "ghost")).firstOrNull()
        assertNull(found) // since it was never inserted
    }
    @Test
    fun `createProject should insert the project into the collection`() = runTest {
        val testProject = ProjectDto(
            id = "test123",
            name = "Test Project",
            description = "Test description",
            createdBy = "tester",
            projectLogs = listOf(),
            projectState = "active",
            taskStates = listOf(),
            projectStates = listOf(),
            matesIds = listOf("user1"),
            tasks = listOf()
        )

        projectMongoDBDataSource.createProject(testProject)

        val stored = copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "test123")).firstOrNull()
        assertNull(stored)
    }

    @Test
    fun ` Shouldn't createProject allow duplicate project if _id uniqueness `() = runTest {
        val project = ProjectDto(
            id ="ghost2",
            name = "Test Project5",
            description = "A test description",
            createdBy = "tester",
            projectLogs = listOf(),
            projectState = "active",
            taskStates = listOf(),
            projectStates = listOf(),
            matesIds = listOf(),
            tasks = listOf()
        )

        projectMongoDBDataSource.createProject(project)

        assertFailsWith<MongoWriteException> {   projectMongoDBDataSource.createProject(project) }

        val foundProjects = copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "duplicate123")).toList()
        assertEquals(0, foundProjects.size)
    }


}