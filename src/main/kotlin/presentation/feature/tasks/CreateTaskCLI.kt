package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.CreateTaskUseCase
import kotlinx.coroutines.runBlocking
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createTaskUseCase: CreateTaskUseCase,
    private val createLogUseCase: CreateLogUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase

) {
    fun show() {
       runBlocking {
           outputPrinter.printMessage("=== Create Task ===")
           try {
               val task = readTaskInput()
               createTaskUseCase.createTask(task)
               outputPrinter.printMessage("Task created successfully")
           } catch (exception: Exception) {
               outputPrinter.printMessage(exception.message.toString())
               outputPrinter.printMessage("Failed to create task")
           }
       }
    }

    private fun readTaskInput(): Task {
        val projectId = inputReader.readInput("Enter project ID: ")
        val title = inputReader.readInput("Enter task title: ")
        val description = inputReader.readInput("Enter task description: ")
        val project = getProjectByIdUseCase.invoke(projectId)

        outputPrinter.printMessage("Available task states:")
        project.taskStates.forEachIndexed { index, state ->
            outputPrinter.printMessage("${index + 1}. $state")
        }

        val selectIndex = inputReader.readInput("Select task state: ").toIntOrNull() ?: 1
        val selectedState = project.taskStates.getOrElse(selectIndex - 1) { project.taskStates.first() }

        return Task(
            projectId = projectId,
            title = title,
            description = description,
            taskState = selectedState,
            createdBy = CurrentUser.getCurrentUser()?.username ?: "Unknown",
            logs = listOf(
                createLogUseCase.invoke(
                    operationType = OperationType.CREATE,
                    entityName = title,
                    entityType = EntityType.TASK,
                    username = CurrentUser.getCurrentUser()!!.username,
                )
            ),
            id = UUID.randomUUID()
        )
    }
}