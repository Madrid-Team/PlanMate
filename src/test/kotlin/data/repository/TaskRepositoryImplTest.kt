package data.repository

import data.source.task.TaskExternalDataSource
import data.source.task.helperTaskDto
import domain.models.task.Task
import domain.repository.TaskRepository
import domain.usecases.task.createTask
import domain.utlis.TaskExceptions
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskExternalDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var testScope:TestScope

    @BeforeEach
    fun setup() {
        taskDataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(taskDataSource)
        testScope = TestScope()
    }

    @Test
    fun `editTask should pass edit task successfully to data source`() {
        testScope.launch {
            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
            val updatedTask = helperTaskDto(id = UUID.randomUUID().toString(), title = "task")

//        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
//            every { taskDataSource.editTask(updatedTask) } returns Unit

            assertDoesNotThrow { taskDataSource.editTask(updatedTask) }
        }
    }

    @Test
    fun `editTask should throw exception when failed to edit task`() {
        testScope.launch {
            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
            val tasks = listOf(task)
//        every { taskMemoryDataSource.editTask(task) } returns listOf(task)
//        every { taskDataSource.editTask(tasks.map { it.toDto() }) } throws IOException()

            assertThrows<Exception> {
                taskRepository.editTask(task)
            }
        }
    }


    @Test
    fun `getAllTask should return list of tasks when data source is not empty`() {
       testScope.launch {
           val tasks = listOf(createTask(), createTask())
//           assertDoesNotThrow { taskRepository.getAllTasks() }
       }
    }


    @Test
    fun `getAllTask should throw exception when data source throw exception`() {
//        every { taskMemoryDataSource.getTasks() } returns listOf()
        testScope.launch {
//            assertThrows<TaskExceptions.TaskNotFoundException> { taskRepository.getAllTasks() }
        }
    }


    @Test
    fun `getTasksByProjectId should return list of taskDto data source return success`() {
        testScope.launch {
            assertDoesNotThrow { taskDataSource.getTasksByProjectId(projectId) }
        }
    }

    @Test
    fun `getTaskByProjectID should throw exception tasks return from data source is empty`() {

     testScope.launch {
         assertThrows<TaskExceptions.TaskNotFoundException> { taskRepository.getTasksByProjectId(projectId) }
     }
    }

    @Test
    fun `createTask should execute successfully when data source create task successfully`() {
        testScope.launch {
            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
//            every { taskDataSource.createTask(task.toDto()) } returns Unit

            assertDoesNotThrow { taskRepository.createTask(task) }
        }
    }

    @Test
    fun `createTask should throw exception when data source throw exception`() {
        testScope.launch {
            val task = createTask(id = UUID.randomUUID().toString(), title = "task")
//            every { taskDataSource.createTask(task.toDto()) } throws IOException()

            assertThrows<Exception> { taskRepository.createTask(task) }
        }
    }


    @Test
    fun `deleteTask should execute successfully when data source delete task successfully`() {
      testScope.launch {
          val taskId = UUID.randomUUID().toString()
          val projectId = UUID.randomUUID().toString()
//          every { taskDataSource.deleteTask(tasks) } returns Unit

          assertDoesNotThrow { taskRepository.deleteTask(projectId,taskId) }
      }
    }

    @Test
    fun `deleteTask should throw exception when data source fails to delete`() {
      testScope.launch {
          val taskId = UUID.randomUUID().toString()
          val projectId = UUID.randomUUID().toString()
          val tasks = emptyList<Task>()
//          every { taskMemoryDataSource.deleteTask(taskId) } returns tasks
//          every { taskDataSource.deleteTask(tasks.map { it.toDto() }) } throws IOException()


          assertThrows<TaskExceptions> { taskRepository.deleteTask(projectId,taskId) }
      }
    }

    @Test
    fun `getTaskLogsById returns task's logs when it exists in tasks list`() {
       testScope.launch {
           val id = UUID.randomUUID().toString()
           val projectId = UUID.randomUUID().toString()
           assertDoesNotThrow { taskRepository.getTaskLogsByID(projectId,id) }
       }
    }


    @Test
    fun `getTaskLogsByID throw Task not found exception when task  does not exist in tasks list`() {
      testScope.launch {
          val id = UUID.randomUUID().toString()
          val projectId= UUID.randomUUID().toString()
          val tasks = listOf(
              helperTaskDto(id = id, projectId, logs = listOf("ahmed added a task", "ahmed deleted a task"))
          )

          assertThrows<TaskExceptions.NoLogsFoundException> { taskRepository.getTaskLogsByID(projectId,id) }


      }
    }

    companion object {
        val projectId = UUID.randomUUID().toString()
        val tasks = listOf(
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test"),
            helperTaskDto(id = UUID.randomUUID().toString(), title = "test2")
        )
    }
}