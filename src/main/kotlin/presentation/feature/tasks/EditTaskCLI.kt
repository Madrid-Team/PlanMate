package presentation.feature.tasks

import domain.models.task.Task
import domain.usecases.task.EditTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val editTaskUseCase: EditTaskUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Edit Task ===")

        val projectId = inputReader.readInput("Enter project ID: ")
        val taskId = inputReader.readInput("Enter task ID: ")
        val title = inputReader.readInput("Enter new title: ")
        val description = inputReader.readInput("Enter new description: ")

        val updatedTask = Task(
            id = taskId,
            projectId = projectId,
            title = title,
            description = description,
            state = "",
            createdBy = "",
            logs = listOf()
        )

        val result = editTaskUseCase.editTask(taskId, updatedTask)

        if (result) {
            outputPrinter.printMessage("Task updated successfully")
        } else {
            outputPrinter.printMessage("Failed to update task")
        }

    }
}