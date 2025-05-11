package data.source.project

import com.mongodb.MongoWriteException
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.project.ProjectDto
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import domain.utlis.ProjectExceptions
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
import data.source.mongoDb.MongoClientProvider
import java.util.*
import kotlin.test.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var projectMongoDBDataSource: ExternalProjectDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    val testScope = TestScope()


    @BeforeAll
    fun setup() {
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database, "projects_test", "projects")
        runBlocking {
            projectMongoDBDataSource =
                ProjectMongoDBDataSource(database.getCollection<ProjectDto>("projects_test"))
        }


    }


    val projectId = UUID.randomUUID().toString()
    val logs = listOf("Log 1", "Log 2")
    val project = ProjectDto(
        id = projectId,
        name = "Existing Project",
        description = "Test project",
        createdBy = "tester",
        projectLogs = logs,
        projectState = "active",
        taskStates = emptyList(),
        projectStates = emptyList(),
        matesUsernames = emptyList(),
        tasks = emptyList()
    )
    val testProject = ProjectDto(
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
    fun `createProject should call insertOne with correct project`() = runTest {
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


    @Test
    fun `createProject adds a project and getProjects returns it`() {

            var projects: List<ProjectDto>
            val newProject = ProjectDto(
                id = UUID.randomUUID().toString(),
                name = "Test Project",
                description = "A test description",
                createdBy = "Abdo",
                projectLogs = listOf(),
                projectState = "active",
                taskStates = listOf(),
                projectStates = listOf(),
                matesUsernames = listOf(),
                tasks = listOf()
            )

            val testUser = User(id = UUID.randomUUID(), "Abdo", "sgsgsggs", UserRole.ADMIN.name)
        CurrentUser.setCurrentUser(user = testUser)
        runTest {

            projectMongoDBDataSource.createProject(newProject)

            projects = projectMongoDBDataSource.getProjects()
            println(projects)

            assertEquals(true, projects.any { it.name == "Test Project" })

        }

       }


    @Test
    fun `createProject should insert successfully the project into the collection`() {
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
        testScope.launch {
            projectMongoDBDataSource.createProject(testProject)

            val stored = copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "test123"))
            assertNotNull(stored)
        }
    }

    @Test
    fun ` Shouldn't createProject allow duplicate project if _id uniqueness `() {
        val projectId = UUID.randomUUID().toString()
        val logs = listOf("Log 1", "Log 2")
        val project = ProjectDto(
            id = projectId,
            name = "Existing Project",
            description = "Test project",
            createdBy = "tester",
            projectLogs = logs,
            projectState = "active",
            taskStates = emptyList(),
            projectStates = emptyList(),
            matesUsernames = emptyList(),
            tasks = emptyList()
        )
        runTest {
            projectMongoDBDataSource.createProject(project)

            assertFailsWith<MongoWriteException> { projectMongoDBDataSource.createProject(project) }

            val foundProjects =
                copyCollectionIfDifferentToTest.copyCollectionIfDifferent().find(Filters.eq("_id", "duplicate123"))
                    .toList()
            assertEquals(0, foundProjects.size)
        }
    }


    @Test
    fun `editProject should not update if project does not exist`() {

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
        runTest {
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


    @Test
    fun `deleteProject should delete project with given projectId`() {
        testScope.launch {

            val testUser = User(id = UUID.randomUUID(), "hhdhdh", "sgsgsggs", UserRole.ADMIN.name)
            mockkObject(CurrentUser)
            every { CurrentUser.getCurrentUser() } returns testUser


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
    fun `getProjectLogsById should returns logs when project exists`() = runTest {
        val projectId = UUID.randomUUID().toString()
        val logs = listOf("Log 5", "Log 7")
        val project = ProjectDto(
            id = projectId,
            name = "Existing Project",
            description = "Test project",
            createdBy = "tester",
            projectLogs = logs,
            projectState = "active",
            taskStates = emptyList(),
            projectStates = emptyList(),
            matesUsernames = emptyList(),
            tasks = emptyList()
        )
        projectMongoDBDataSource.createProject(project)

        val resultLogs = projectMongoDBDataSource.getProjectLogsById(projectId)

        assertEquals(logs, resultLogs)
    }
    @Test
    fun `getProjectLogsById returns empty list when project does not exist`() = runTest {
        val nonExistentId = UUID.randomUUID().toString()

        val logs = projectMongoDBDataSource.getProjectLogsById(nonExistentId)

        assertTrue(logs?.isEmpty() ?: false)
    }


    @Test
    fun `getProjectById returns the correct project when it exists`() = runTest {


        projectMongoDBDataSource.createProject(project)

        val retrieved = projectMongoDBDataSource.getProjectById(projectId)

        assertEquals(projectId, retrieved?.id)
        assertEquals("Existing Project", retrieved?.name)
    }

    @Test
    fun `getProjectById throws ProjectNotFoundException when project does not exist`() = runTest {
        val nonExistentId = "id Dose not exist-different format from UUID-"

        val exception  = assertFailsWith<ProjectExceptions.ProjectNotFoundException> {
            projectMongoDBDataSource.getProjectById(nonExistentId)
        }

        assertTrue(exception is ProjectExceptions.ProjectNotFoundException)
    }


    @AfterAll
    fun cleanup() {
        mongoClientProvider.close()

    }

}