package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.task.CreateTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val createTaskUseCase: CreateTaskUseCase,
    private val createLogUseCase: CreateLogUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Create Task ===")

        val task = readTaskInput()

        val result = createTaskUseCase.createTask(task)

        printResult(result)
    }

    private fun readTaskInput(): Task {
        val projectId = inputReader.readInput("Enter project ID: ")
        val title = inputReader.readInput("Enter task title: ")
        val description = inputReader.readInput("Enter task description: ")

        return Task(
            projectId = projectId,
            title = title,
            description = description,
            taskState = "",
            createdBy = "",
            logs =  listOf(
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

    private fun printResult(result: Boolean) {
        if (result) {
            outputPrinter.printMessage("Task created successfully")
        } else {
            outputPrinter.printMessage("Failed to create task")
        }
    }

}