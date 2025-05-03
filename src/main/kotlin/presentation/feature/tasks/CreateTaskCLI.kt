package presentation.feature.tasks

import domain.models.logs.CurrentUser
import domain.models.task.Task
import domain.usecases.task.CreateTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*
import java.util.*

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val createTaskUseCase: CreateTaskUseCase
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
            state = "",
            createdBy = CurrentUser.getCurrentUser()?.username ?: "Unknown",
            logs = listOf(),
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