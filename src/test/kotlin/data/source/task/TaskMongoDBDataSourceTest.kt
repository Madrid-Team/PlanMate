package data.source.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.task.TaskDto
import data.source.TaskExternalDataSource
import data.source.mongoDb.TaskMongoDBDataSource
import data.source.mongoDb.MongoClientProvider
import data.utils.TASK_PROJECT_ID
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.bson.Document
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var taskMongoDBDataSource: TaskExternalDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    private lateinit var testScope: TestScope
    private val collection = mockk<MongoCollection<TaskDto>>()

    @BeforeAll
    fun setup() {
        testScope = TestScope()
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database, "projects")
        runBlocking {
            taskMongoDBDataSource =
                TaskMongoDBDataSource(database.getCollection<TaskDto>("Collection_Test"))
        }
    }

    val task = TaskDto(
        id = "task-id",
        projectId = "project-id",
        title = "Updated Task",
        description = "Updated Desc",
        taskState = "in-progress",
        createdBy = "user",
        taskLogs = listOf("edited log")
    )
    val task2 = TaskDto(
        id = "task-id2",
        projectId = "project-id",
        title = "Test Task",
        description = "Test Desc",
        taskState = "open",
        createdBy = "user",
        taskLogs = emptyList()
    )

    @Test
    fun `createTask should push task to the correct project`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<TaskDto>>(relaxed = true)

        val filter = eq("_id", task2.projectId)
        val update = Updates.push("tasks", task2)

        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L

        coEvery { collection.updateOne(filter, update) } returns updateResult

        val dataSource = TaskMongoDBDataSource(collection)

        // Act
        dataSource.createTask(task2)
        testScope.launch {
            // Assert
            coVerify(exactly = 1) { collection.updateOne(filter, update) }
        }

    }

    @Test
    fun `createTask throws exception on database error`() {
        runTest {
            // Arrange
            val projectId = "project1"
            val task = TaskDto(
                id = "task1",
                projectId = projectId,
                title = "Task 1",
                description = "Description 1",
                taskState = "TODO",
                createdBy = "user1",
                taskLogs = listOf("log1", "log2")
            )

            coEvery {
                collection.updateOne(
                    eq("_id", "false projectId"),
                    Updates.push("tasks", task)
                )
            } throws Exception()
            // Act & Assert
            assertThrows<Exception> {
                taskMongoDBDataSource.createTask(task)
            }
        }
    }

    @Test
    fun `editTask should update the matching task in the project`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<TaskDto>>(relaxed = true)

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
        val collection = mockk<MongoCollection<TaskDto>>(relaxed = true)
        val projectId = "project-id"
        val taskId = "task-id"

        val filter = eq("_id", projectId)
        val update = Updates.pull("tasks", Document("id", taskId))

        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 1L
        coEvery { collection.updateOne(filter, update) } returns updateResult

        val dataSource = TaskMongoDBDataSource(collection)

        // Act
        dataSource.deleteTask(taskId)
        testScope.launch {
            // Assert
            coVerify(exactly = 1) { collection.updateOne(filter, update) }
        }
    }


    @Test
    fun `getTasksByProjectId returns empty list when no project found`() = runTest {
        // Given
        val projectId = "nonexistent"
        val collection = mockk<MongoCollection<TaskDto>>(relaxed = true)
        val taskMongoDBDataSource = TaskMongoDBDataSource(collection)
        val mockFlow = mockk<FindFlow<TaskDto>>(relaxed = true)
        coEvery { collection.find<TaskDto>(Filters.eq(TASK_PROJECT_ID, projectId), any()) } returns mockFlow
        coEvery { mockFlow.toList() } returns emptyList()

        // When
        val result = taskMongoDBDataSource.getTasksByProjectId(projectId)

        // Then
        assertEquals(0, result.size)
    }


    @Test
    fun `deleteTask does nothing when project does not exist`() {
        // Arrange
        val projectId = "nonexistent"
        val taskId = "task1"
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.modifiedCount } returns 0L

        coEvery {
            collection.updateOne(
                eq("_id", projectId),
                Updates.pull("tasks", Document("id", taskId))
            )
        } returns updateResult

        testScope.launch {
            // Act
            taskMongoDBDataSource.deleteTask(taskId)

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
    fun `deleteTask throws exception on database error`() {
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
                taskMongoDBDataSource.deleteTask(taskId)
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


    @Test
    fun `getTaskLogsByID returns empty list when no tasks found`() = runTest {
        // Given
        val taskId = "missingTask"
        val collection = database.getCollection<TaskDto>("Collection_Test")
        taskMongoDBDataSource = TaskMongoDBDataSource(collection)
        // When
        val result = taskMongoDBDataSource.getTaskLogsByID(taskId)

        // Then
        assertTrue(result.isEmpty())

    }

    @AfterAll
    fun cleanup() {
        clearAllMocks()
        mongoClientProvider.close()

    }
}