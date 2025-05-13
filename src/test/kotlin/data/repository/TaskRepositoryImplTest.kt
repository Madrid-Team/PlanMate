package data.repository

import data.mapper.toDto
import data.source.task.TaskExternalDataSource
import data.source.task.TaskManager
import data.source.task.helperTaskDto
import domain.repository.TaskRepository
import domain.usecases.task.createTask
import domain.utils.PlanMateExceptions
import domain.utils.TaskExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.util.*

//class TaskRepositoryImplTest {
//    private lateinit var taskExternalDataSource: TaskExternalDataSource
//    private lateinit var taskRepository: TaskRepository
//    private lateinit var testScope: TestScope
//    private lateinit var taskManager: TaskManager
//
//    @BeforeEach
//    fun setup() {
//        taskExternalDataSource = mockk(relaxed = true)
//        taskRepository = TaskRepositoryImpl(taskExternalDataSource)
//        testScope = TestScope()
//        taskManager = mockk(relaxed = true)
//    }
//
//    @Test
//    fun `editTask should pass edit task successfully to data source`() {
//        testScope.runTest {
//            val task = helperTaskDto(id = UUID.randomUUID().toString(), title = "task")
//            assertDoesNotThrow { taskExternalDataSource.editTask(task) }
//        }
//    }
//
//    @Test
//    fun `editTask should throw exception when failed to edit task`() {
//        testScope.runTest {
//            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
//            coEvery { taskManager.editTask(task.toDto()) } returns listOf(task.toDto())
//            coEvery { taskExternalDataSource.editTask(task.toDto()) } throws IOException()
//
//            assertThrows<Exception> {
//                taskRepository.editTask(task)
//            }
//        }
//    }
//
//    @Test
//    fun `getTasksByProjectId should return list of taskDto data source return success`() {
//        testScope.runTest {
//            assertDoesNotThrow { taskExternalDataSource.getTasksByProjectId(projectId) }
//        }
//    }
//
//    @Test
//    fun `getTaskByProjectID should throw exception tasks return from data source is empty`() {
//        coEvery { taskExternalDataSource.getTasksByProjectId(projectId) } throws Exception()
//        testScope.runTest {
//            assertThrows<PlanMateExceptions> { taskRepository.getTasksByProjectId(projectId) }
//        }
//    }
//
//    @Test
//    fun `createTask should execute successfully when data source create task successfully`() {
//        testScope.runTest {
//            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
//
//            assertDoesNotThrow { taskRepository.createTask(task) }
//        }
//    }
//
//    @Test
//    fun `createTask should throw exception when data source throw exception`() {
//        testScope.runTest {
//            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
//            coEvery { taskExternalDataSource.createTask(task.toDto()) } throws IOException()
//
//            assertThrows<Exception> { taskRepository.createTask(task) }
//        }
//    }
//
//
//    @Test
//    fun `deleteTask should execute successfully when data source delete task successfully`() {
//        testScope.runTest {
//            val taskId = UUID.randomUUID().toString()
//            val projectId = UUID.randomUUID().toString()
//            coEvery { taskExternalDataSource.deleteTask(taskId = taskId, projectId = projectId) } returns Unit
//
//            assertDoesNotThrow { taskRepository.deleteTask(projectId, taskId) }
//        }
//    }
//
//    @Test
//    fun `deleteTask should throw exception when data source fails to delete`() {
//        testScope.runTest {
//            val taskId = UUID.randomUUID().toString()
//            val projectId = UUID.randomUUID().toString()
//            coEvery { taskExternalDataSource.deleteTask(taskId = taskId, projectId = projectId) } throws IOException()
//
//            assertThrows<TaskExceptions> { taskRepository.deleteTask(projectId, taskId) }
//        }
//    }
//
//    @Test
//    fun `getTaskLogsById returns task's logs when it exists in tasks list`() {
//        testScope.runTest {
//            val id = UUID.randomUUID().toString()
//            val projectId = UUID.randomUUID().toString()
//
//            assertDoesNotThrow { taskRepository.getTaskLogsByID(projectId, id) }
//        }
//    }
//
//
//    @Test
//    fun `getTaskLogsByID throw Task not found exception when task  does not exist in tasks list`() {
//        testScope.runTest {
//            val taskId = UUID.randomUUID().toString()
//            val projectId = UUID.randomUUID().toString()
//            coEvery {
//                taskExternalDataSource.getTaskLogsByID(
//                    taskId = taskId,
//                    projectId = projectId
//                )
//            } throws Exception()
//
//            assertThrows<PlanMateExceptions> { taskRepository.getTaskLogsByID(projectId, taskId) }
//        }
//    }
//
//    companion object {
//        val projectId = UUID.randomUUID().toString()
//        val tasks = listOf(
//            helperTaskDto(id = UUID.randomUUID().toString(), title = "test"),
//            helperTaskDto(id = UUID.randomUUID().toString(), title = "test2")
//        )
//    }
//}