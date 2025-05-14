package data.source.project

import com.mongodb.MongoWriteException
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.project.ProjectDto
import data.source.csv.project.ProjectExternalDataSource
import data.source.csv.project.ProjectMongoDBDataSource
import data.source.mongoDb.MongoClientProvider
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import io.mockk.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var projectMongoDBDataSource: ProjectExternalDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    val testScope = TestScope()


    @BeforeAll
    fun setup() {
        mongoClientProvider = mockk(relaxed = true)
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database, "projects_test", "projects")
        runBlocking {
            projectMongoDBDataSource =
                ProjectMongoDBDataSource(database.getCollection<ProjectDto>("projects_test"))
        }
    }

    private val testProject = ProjectDto(
        id = UUID.randomUUID().toString(),
        name = "Test Project For create",
        description = "A test description",
        createdBy = "tester",
        projectLogs = emptyList(),
        projectState = "active",
        taskStates = emptyList(),
        projectStates = emptyList(),
        matesUsernames = emptyList(),
        tasks = emptyList()
    )

    @Test
    fun `createProject should call insertOne with correct project`() {
        runTest {
            // Arrange
            val collection = mockk<MongoCollection<ProjectDto>>()

            coEvery {
                collection.insertOne(eq(testProject), any())
            } returns mockk()

            val dataSource = ProjectMongoDBDataSource(collection)

            dataSource.createProject(testProject)

            coVerify(exactly = 1) {
                collection.insertOne(eq(testProject), any())
            }
        }
    }

    @Test
    fun `createProject adds a project and getProjects returns it`() {
        testScope.launch {
            var projects: List<ProjectDto> = emptyList()
            val newProject = ProjectDto(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "A test description",
                createdBy = "tester",
                projectLogs = listOf(),
                projectState = "active",
                taskStates = listOf(),
                projectStates = listOf(),
                matesUsernames = listOf(),
                tasks = listOf()
            )
            val testUser = User(id = UUID.randomUUID(), "hhdhdh", "sgsgsggs", UserRole.ADMIN.name)
            mockkObject(CurrentUser)
            every { CurrentUser.getCurrentUser() } returns testUser

            projectMongoDBDataSource.createProject(newProject)

            projects = projectMongoDBDataSource.getProjects(mockk())

            assertEquals(true, projects.any { it.name == "Test Project" })

        }
    }


    @Test
    fun `deleteProject should delete project with given projectId`() {
        testScope.launch {

            val testUser = User(id = UUID.randomUUID(), "hhdhdh", "sgsgsggs", UserRole.ADMIN.name)
            mockkObject(CurrentUser)
            every { CurrentUser.getCurrentUser() } returns testUser


            //Given
            val projectId = projectMongoDBDataSource.getProjects(mockk()).first().id


            // When
            projectMongoDBDataSource.deleteProject(projectId)

            // Then
            assertEquals(0, projectMongoDBDataSource.getProjects(mockk()).size)

        }
    }

    @Test
    fun `deleteProject should call deleteOne with correct filter`() = runTest {
        // Given
        val projectId = "ghost2"

        val collection = mockk<MongoCollection<ProjectDto>>()
        val dataSource = ProjectMongoDBDataSource(collection)

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
    fun `editProject should not update if project does not exist`() {
        testScope.launch {
            // Given
            val nonExistentProject = ProjectDto(
                id = "ghost",
                name = "Test Project5",
                description = "A test description",
                createdBy = "tester",
                projectLogs = listOf(),
                projectState = "active",
                taskStates = listOf(),
                projectStates = listOf(),
                matesUsernames = listOf(),
                tasks = listOf()
            )

            // When
            val result = projectMongoDBDataSource.editProject(nonExistentProject)

            // Then
            val found =
                copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "ghost"))
                    .firstOrNull()
            assertNull(found)
        }
    }

    @Test
    fun `createProject should insert the project into the collection`() {
        testScope.launch {
            val testProject = ProjectDto(
                id = "test123",
                name = "Test Project",
                description = "Test description",
                createdBy = "tester",
                projectLogs = listOf(),
                projectState = "active",
                taskStates = listOf(),
                projectStates = listOf(),
                matesUsernames = listOf("user1"),
                tasks = listOf()
            )

            projectMongoDBDataSource.createProject(testProject)

            val stored = copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "test123"))
            assertNotNull(stored)
        }
    }

    @Test
    fun ` Shouldn't createProject allow duplicate project if _id uniqueness `() {
        testScope.launch {
            val project = ProjectDto(
                id = "ghost2",
                name = "Test Project5",
                description = "A test description",
                createdBy = "tester",
                projectLogs = listOf(),
                projectState = "active",
                taskStates = listOf(),
                projectStates = listOf(),
                matesUsernames = listOf(),
                tasks = listOf()
            )

            projectMongoDBDataSource.createProject(project)

            assertFailsWith<MongoWriteException> { projectMongoDBDataSource.createProject(project) }

            val foundProjects =
                copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "duplicate123"))
                    .toList()
            assertEquals(0, foundProjects.size)
        }
    }

    @Test
    fun `editProject should call replaceOne with correct query and project`() = runTest {
        val collection = mockk<MongoCollection<ProjectDto>>()
        val testProject = ProjectDto(
            id = UUID.randomUUID().toString(),
            name = "Edited Project",
            description = "Updated description",
            createdBy = "tester",
            projectLogs = emptyList(),
            projectState = "active",
            taskStates = emptyList(),
            projectStates = emptyList(),
            matesUsernames = emptyList(),
            tasks = emptyList()
        )

        val expectedQuery = Filters.eq("_id", testProject.id)


        coEvery {
            collection.replaceOne(expectedQuery, testProject, any())
        } returns mockk()

        val dataSource = ProjectMongoDBDataSource(collection)

        dataSource.editProject(testProject)

        coVerify(exactly = 1) {
            collection.replaceOne(expectedQuery, testProject, any())
        }
    }

    @AfterAll
    fun cleanup() {
        mongoClientProvider.close()

    }
}