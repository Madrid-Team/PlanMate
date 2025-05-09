package data.source.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.project.ProjectDto
import data.dto.task.TaskDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.madrid.data.source.mongoDb.MongoClientProvider
import org.madrid.data.source.project.ProjectMongoDBDataSource
import org.madrid.data.source.task.TaskMongoDBDataSource
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var taskMongoDBDataSource: ExternalTaskDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    private lateinit var testScope: TestScope
    private val collection = mockk<MongoCollection<TaskDto>>()
    @BeforeAll
    fun setup() {
        testScope = TestScope()
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database)
        runBlocking {
            taskMongoDBDataSource =
                TaskMongoDBDataSource(database.getCollection<ProjectDto>("projects_test") )
        }


    }
    val projectId = "some-project-id"
    val taskId = "task1"

    val sampleTask = TaskDto(
        id = taskId,
        projectId = "2",
        title = "title",
        description = "description",
        taskState = "state",
        createdBy = "created by",
        logs = listOf("log1", "log2", "log3")
    )
    val projectDto = ProjectDto(
        id = projectId,
        name = "Project X",
        description = "Test",
        createdBy = "user",
        projectLogs = emptyList(),
        projectState = "active",
        taskStates = emptyList(),
        projectStates = emptyList(),
        matesUsernames = emptyList(),
        tasks = listOf(sampleTask)
    )
    @Test
    fun `editTask should update the matching task in the project`()=runTest  {
        // Arrange
        val collection = mockk<MongoCollection<ProjectDto>>(relaxed = true)
        val task = TaskDto(
            id = "task-id",
            projectId = "project-id",
            title = "Updated Task",
            description = "Updated Desc",
            taskState = "in-progress",
            createdBy = "user",
            logs = listOf("edited log")
        )

        val filter = Filters.and(
            eq("_id", task.projectId),
            eq("tasks.id", task.id)
        )
        val update = Updates.set("tasks.$", task)

        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L
        coEvery { collection.updateOne(filter, update) } returns updateResult

        val dataSource = TaskMongoDBDataSource(collection)

        // Act
        dataSource.editTask(task)
        testScope.launch {
        // Assert
        coVerify(exactly = 1) { collection.updateOne(filter, update) }
    }
    }

    @Test
    fun `deleteTask should remove the task with given ID from project`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<ProjectDto>>(relaxed = true)
        val projectId = "project-id"
        val taskId = "task-id"

        val filter = eq("_id", projectId)
        val update = Updates.pull("tasks", Document("id", taskId))

        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L
        coEvery { collection.updateOne(filter, update) } returns updateResult

        val dataSource = TaskMongoDBDataSource(collection)

        // Act
        dataSource.deleteTask(projectId, taskId)
        testScope.launch {
            // Assert
            coVerify(exactly = 1) { collection.updateOne(filter, update) }
        }
    }
    @Test
    fun `createTask should push task to the correct project`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<ProjectDto>>(relaxed = true)
        val task = TaskDto(
            id = "task-id",
            projectId = "project-id",
            title = "Test Task",
            description = "Test Desc",
            taskState = "open",
            createdBy = "user",
            logs = emptyList()
        )
        val filter = eq("_id", task.projectId)
        val update = Updates.push("tasks", task)

        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L

        coEvery { collection.updateOne(filter, update) } returns updateResult

        val dataSource = TaskMongoDBDataSource(collection)

        // Act
        dataSource.createTask(task)
        testScope.launch {
        // Assert
        coVerify(exactly = 1) { collection.updateOne(filter, update) }
    }

    }
    @Test
    fun `getTaskLogsByID should return empty list if no matching project exists`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<ProjectDto>>()
        val projectId = "project-missing"
        val mockFlow = mockk<FindFlow<ProjectDto>>()// No projects returned

        coEvery { collection.find(eq("_id", projectId)) } returns mockFlow

        val dataSource = TaskMongoDBDataSource(collection)
        testScope.launch {
            // Act
            val logs = dataSource.getTaskLogsByID(projectId, "any-task")

            // Assert
            assertTrue(logs.isEmpty())
            coVerify(exactly = 1) { collection.find(eq("_id", projectId)) }
        }
    }

    @Test
    fun `getTaskLogsByID should return correct logs for a given projectId and taskId`()=runTest {


            val collection = mockk<MongoCollection<ProjectDto>>()
            val insertResult = mockk<InsertOneResult>()
            coEvery { collection.insertOne(any<ProjectDto>(), any()) } returns insertResult

            val mockFlow = mockk<FindFlow<ProjectDto>>()
            coEvery { collection.find(eq("_id", projectId)) } returns mockFlow
            coEvery { mockFlow.collect(any()) } coAnswers {
                val collector = arg<suspend (ProjectDto) -> Unit>(0)
                collector(projectDto)
            }

            val projectDataSource = ProjectMongoDBDataSource(collection)
            val taskDataSource = TaskMongoDBDataSource(collection)

           projectDataSource.createProject(projectDto)
        testScope.launch {
           val result = taskDataSource.getTaskLogsByID(projectId, taskId)

           assertEquals(listOf("log1", "log2", "log3"), result)

           coVerify { collection.insertOne(any<ProjectDto>(), any()) }
           coVerify { collection.find(eq("_id", projectId)) }
           coVerify { mockFlow.collect(any()) }
       }

}


    @Test
    fun `getTasksByProjectId returns empty list when no project found`()  {
        // Given
        val projectId = "nonexistent"
      val collection = mockk<MongoCollection<ProjectDto>>()
        val mockFlow = mockk<FindFlow<ProjectDto>>()
        coEvery { collection.find(eq("_id", projectId)) } returns mockFlow
runTest {
        // When
        val result = taskMongoDBDataSource.getTasksByProjectId(projectId)

        // Then
        assertEquals(0, result.size)
    }
        }
    @Test
    fun `createTask should add the task to the project tasks list`() {
        //Given
        val filter = eq("_id", sampleTask.projectId)
        val update = Updates.push("tasks", sampleTask)
        testScope.launch {
            coEvery { collection.updateOne(filter, update) } returns mockk()

            // when
            taskMongoDBDataSource.createTask(sampleTask)

            // then
            coVerify { collection.updateOne(filter, update) }
        }
    }
    @Test
    fun `deleteTask should remove the task from the project tasks list`() {
        val collection = mockk<MongoCollection<ProjectDto>>()

        val projectId = "some-project-id"
        val taskId = "task-to-delete"

        val filter = eq("_id", projectId)
        val update = Updates.pull("tasks", Document("id", taskId))
        testScope.launch {
            // Mock updateOne call
            coEvery { collection.updateOne(filter, update) } returns mockk()

            val projectDataSource = ProjectMongoDBDataSource(collection)

            // Perform the delete
            taskMongoDBDataSource.deleteTask(projectId, taskId)

            // Verify that updateOne was called with the correct filter and update
            coVerify { collection.updateOne(filter, update) }
        }
    }


    // Tests for deleteTask
    @Test
    fun `deleteTask successfully removes task when project and task exist`() {
        // Arrange
        val projectId = "project1"
        val taskId = "task1"
        val updateResult = mockk<UpdateResult>()

        coEvery { updateResult.modifiedCount } returns 1L
        coEvery { collection.updateOne(eq("_id", projectId), Updates.pull("tasks", Document("id", taskId))) } returns updateResult

        testScope.launch {
            // Act
        taskMongoDBDataSource.deleteTask(projectId, taskId)

        // Assert
        coVerify {
            collection.updateOne(
                eq("_id", projectId),
                Updates.pull("tasks", Document("id", taskId))
            )
        }
        assertEquals(1L, updateResult.modifiedCount)
    }
    }

    @Test
    fun `deleteTask does nothing when project does not exist`()  {
        // Arrange
        val projectId = "nonexistent"
        val taskId = "task1"
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 0L

        coEvery { collection.updateOne(eq("_id", projectId), Updates.pull("tasks", Document("id", taskId))) } returns updateResult

        testScope.launch {
            // Act
        taskMongoDBDataSource.deleteTask(projectId, taskId)

        // Assert
        coVerify {
            collection.updateOne(
                eq("_id", projectId),
                Updates.pull("tasks", Document("id", taskId))
            )
        }
        assertEquals(0L, updateResult.modifiedCount)
    }
    }

    @Test
    fun `deleteTask throws exception on database error`() = runTest {
        // Arrange
        val projectId = "project1"
        val taskId = "task1"
       testScope.launch {
           coEvery {
               collection.updateOne(
                   eq("_id", projectId),
                   Updates.pull("tasks", Document("id", taskId))
               )
           } throws RuntimeException("DB error")

           // Act & Assert
           val thrown = assertThrows<RuntimeException> {
               taskMongoDBDataSource.deleteTask(projectId, taskId)
           }
           assertEquals("DB error", thrown.message)
           coVerify {
               collection.updateOne(
                   eq("_id", projectId),
                   Updates.pull("tasks", Document("id", taskId))
               )
           }
       }
    }

    // Tests for createTask
    @Test
    fun `createTask successfully adds task to project`() = runTest {
        // Arrange
        val projectId = "project1"
        val task = TaskDto(
            id = "task1",
            projectId = projectId,
            title = "Task 1",
            description = "Description 1",
            taskState = "TODO",
            createdBy = "user1",
            logs = listOf("log1", "log2")
        )
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L
        testScope.launch {
            coEvery { collection.updateOne(eq("_id", projectId), Updates.push("tasks", task)) } returns updateResult
            // Act
            taskMongoDBDataSource.createTask(task)
            // Assert
            coVerify {
                collection.updateOne(
                    eq("_id", projectId),
                    Updates.push("tasks", task)
                )
            }
            assertEquals(1L, updateResult.modifiedCount)
        }
    }

    @Test
    fun `createTask does nothing when project does not exist`() = runTest {
    // Arrange
    val projectId = "nonexistent"
    val task = TaskDto(
        id = "task1",
        projectId = projectId,
        title = "Task 1",
        description = "Description 1",
        taskState = "TODO",
        createdBy = "user1",
        logs = listOf("log1", "log2")
    )
    val updateResult = mockk<UpdateResult>()
    coEvery { updateResult.modifiedCount } returns 0L
    testScope.launch {
        coEvery { collection.updateOne(eq("_id", projectId), Updates.push("tasks", task)) } returns updateResult

        // Act
        taskMongoDBDataSource.createTask(task)

        // Assert
        coVerify {
            collection.updateOne(
                eq("_id", projectId),
                Updates.push("tasks", task)
            )
        }
        assertEquals(0L, updateResult.modifiedCount)
    }
}

    @Test
    fun `createTask throws exception on database error`() {
        // Arrange
        val projectId = "project1"
        val task = TaskDto(
            id = "task1",
            projectId = projectId,
            title = "Task 1",
            description = "Description 1",
            taskState = "TODO",
            createdBy = "user1",
            logs = listOf("log1", "log2")
        )

            coEvery { collection.updateOne(eq("_id", projectId), Updates.push("tasks", task)) } throws RuntimeException(
                "DB error"
            )
        testScope.launch {
            // Act & Assert
            val thrown = assertThrows<RuntimeException> {
                taskMongoDBDataSource.createTask(task)
            }
            assertEquals("DB error", thrown.message)
            coVerify {
                collection.updateOne(
                    eq("_id", projectId),
                    Updates.push("tasks", task)
                )
            }
        }
    }
    @AfterAll
    fun cleanup() {
        mongoClientProvider.close()

    }
}