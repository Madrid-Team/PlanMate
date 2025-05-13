package presentation.feature.tasks

import domain.models.task.Task
import domain.usecases.task.GetTasksByProjectIdUseCase
import domain.utils.TaskExceptions
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskViewer(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,

) {
    suspend fun displayAllTasks(projectId:String) {
        try {

            val allTasks = getTasksByProjectIdUseCase.getTaskByProjectId(projectId)

            printTasksByState(allTasks)
        }catch (e: TaskExceptions){
            outputPrinter.printError(e.message.toString())
        }

    }

    fun printTasksByState(tasks: List<Task>) {
         val tasksByState = tasks.groupBy { it.taskState }

         tasksByState.forEach { (state, tasksInState) ->
            println("Tasks in state: $state (${tasksInState.size} tasks)")
            println("------------------------------")

            tasksInState.forEach { task ->
                println("ID: ${task.id}")
                println("Title: ${task.title}")
                println("Description: ${task.description}")
                println("Created by: ${task.createdBy}")
                println("Logs: ${task.logs.size} entries")
                println("------------------------------")
            }
            println("\n")
        }
    }

 }