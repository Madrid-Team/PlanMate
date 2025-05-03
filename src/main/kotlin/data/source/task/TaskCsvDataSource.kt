package data.source.task

import data.dto.task.TaskDto
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import data.utils.projectHeader
import data.utils.taskHeader
import domain.models.task.Task
import domain.utlis.TaskNotFoundException

class TaskCsvDataSource(
    private val taskCsvParser: TaskCsvParser,
    private val fileCsvWriter: FileCsvWriter,
    private val fileCsvReader: FileCsvReader
) : TaskDataSource {
    override fun getAllTasks(): Result<List<TaskDto>> {
        val tasks = mutableListOf<TaskDto>()

        return try {

            fileCsvReader.readCsvFile().forEach { row ->
                tasks.add(taskCsvParser.parseOneRowToTask(row))
            }

            Result.success(tasks)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun createTask(task: TaskDto): Result<Unit> {
        return try {


            val row = taskCsvParser.parseTaskToString(task)
            fileCsvWriter.writeToCsvFile(row)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
        /*.copy(
            logs = listOf(
                CreatedLogFormatter.format(
                    entityName = task.title,
                    entityType = EntityType.TASK,
                    username = task.createdBy,
                )
            )
        )*/


    }

    override fun deleteTask(tasks: List<TaskDto>): Result<Unit> {
        return try {
            var taskFileContentAfterDeletion = String.taskHeader
            tasks.forEach {
                val taskAsString = taskCsvParser.parseTaskToString(it)
                taskFileContentAfterDeletion += taskAsString
            }
            fileCsvWriter.updateCsvFile(taskFileContentAfterDeletion)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun editTask(tasks: List<TaskDto>): Result<Unit> {
        return try {
            var taskFileContentAfterUpdate = String.taskHeader
            tasks.forEach {
                val taskAsString = taskCsvParser.parseTaskToString(it)
                taskFileContentAfterUpdate += taskAsString
            }
            fileCsvWriter.updateCsvFile(taskFileContentAfterUpdate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


//    override fun getListWithDeletedTask(taskId: String): List<Task> {
//        return getAllTasks().let { tasks ->
//            tasks.indexOfFirst { it.id.toString() == taskId }.let { taskIndex ->
//                (tasks.subList(0, taskIndex) + tasks.subList(taskIndex + 1, tasks.size))
//            }
//        }
//    }

    /*override fun getListOfUpdatedList(task: Task): List<Task> {
        return getAllTasks().let { tasks ->
            tasks.indexOfFirst { task.id == it.id }.let { taskIndex ->
                (tasks.subList(0, taskIndex) + task
//                    .copy(
//                    logs = task.logs + createLogsForUpdatedFields(
//                        oldTask =,
//                        updatedTask = task
//                    ))
                        + tasks.subList((taskIndex + 1), tasks.size))
            }
        }
    }*/

    /* override fun getTasksByProjectId(projectId: String): List<Task> {
         return getAllTasks().filter { it.projectId == projectId }
     }*/


    /* private fun createLogsForUpdatedFields(oldTask: Task, updatedTask: Task): List<String> {
         val logs = mutableListOf<String>()
         val timestamp = LocalDateTime.now().convertDateIntoReadableDate()
         if (oldTask.title != updatedTask.title) {
             logs.add(
                 //create log message contains the update on project name
                 UpdatedLogFormatter.format(
                     entityName = oldTask.title,
                     entityType = EntityType.TASK,
                     username = oldTask.createdBy,
                     fieldName = "title",
                     oldValue = oldTask.title,
                     newValue = updatedTask.title,
                     timestamp = timestamp
                 )
             )
         }
         if (oldTask.description != updatedTask.description) {
             logs.add(
                 //create log message contains the update on project name
                 UpdatedLogFormatter.format(
                     entityName = oldTask.title,
                     entityType = EntityType.TASK,
                     username = oldTask.createdBy,
                     fieldName = "description",
                     oldValue = oldTask.description,
                     newValue = updatedTask.description,
                     timestamp = timestamp
                 )
             )
         }

         if (oldTask.taskState != updatedTask.taskState) {
             logs.add(
                 //create log message contains the update on project name
                 UpdatedLogFormatter.format(
                     entityName = oldTask.title,
                     entityType = EntityType.TASK,
                     username = oldTask.createdBy,
                     fieldName = "state",
                     oldValue = oldTask.taskState,
                     newValue = updatedTask.taskState,
                     timestamp = timestamp
                 )
             )
         }

         return logs
     }*/

    /* override fun getLogsByTaskId(taskId: String): List<String> {
         val task = getAllTasks().find { it.id.toString() == taskId }?: throw TaskNotFoundException()
         val taskLogs = task.logs
         if (taskLogs.isEmpty())
             throw NoLogsFoundException()
         return taskLogs
     }*/

}