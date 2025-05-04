package presentation.feature.tasks

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.task.Task
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.usecases.task.EditTaskUseCase
import domain.usecases.task.GetTasksByProjectIdUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val createLogUseCase: CreateLogUseCase
) {

    fun show() {
        outputPrinter.printMessage("=== Edit Task ===")

        val projectId = inputReader.readInput("Enter the Project ID: ")
        val projectResult = getProjectByIdUseCase.invoke(projectId)

        if (projectResult.isFailure) {
            outputPrinter.printMessage("Project not found")
            return
        }

        val project = projectResult.getOrThrow()

        val tasksResult = getTasksByProjectIdUseCase(projectId)
        if (tasksResult.isFailure) {
            outputPrinter.printMessage("No tasks found for this project ID")
            return
        }

        val tasks = tasksResult.getOrThrow()

        tasks.forEachIndexed { index, task ->
            outputPrinter.printMessage("Available Tasks : \n ${index + 1}. Name: ${task.title} | (State: ${task.taskState} | Description: ${task.description} )")
        }

        val selectedIndex = inputReader.readInput("Choose task number to edit:").toIntOrNull()?.minus(1)

        if (selectedIndex == null || selectedIndex !in tasks.indices) {
            outputPrinter.printMessage("Invalid selection.")
            return
        }

        val currentTask = tasks[selectedIndex]
        var updatedTask = currentTask.copy()

        while (true) {
            outputPrinter.printMessage("Choose fields to edit")
            outputPrinter.printMessage("1 - Title")
            outputPrinter.printMessage("2 - Description")
            outputPrinter.printMessage("3 - Task State")
            outputPrinter.printMessage("4 - Finish")

            when (inputReader.readInput("Choose option:")) {
                "1" -> {
                    val newTitle = inputReader.readInput("Enter new title:")
                    updatedTask = updatedTask.copy(title = newTitle)
                }

                "2" -> {
                    val newDescription = inputReader.readInput("Enter new description:")
                    updatedTask = updatedTask.copy(description = newDescription)
                }

                "3" -> {
                    val menu = project.taskStates.mapIndexed { i, state -> "${i + 1}. $state" }.joinToString("\n")
                    while (true) {
                        val choice = inputReader.readInput("Available Task States:\n$menu\nChoose number:")
                        val index = choice.toIntOrNull()?.minus(1)
                        if (index != null && index in project.taskStates.indices) {
                            updatedTask = updatedTask.copy(taskState = project.taskStates[index])
                            break
                        } else {
                            outputPrinter.printMessage("Invalid state choice.")
                        }
                    }
                }

                "4" -> {
                    if (currentTask == updatedTask) {
                        outputPrinter.printMessage("No changes made.")
                        return
                    }

                    val logs = getLogsForTaskChanges(currentTask, updatedTask)
                    val result = editTaskUseCase.editTask(
                        updatedTask.copy(logs = currentTask.logs + logs)
                    )

                    result
                        .onSuccess { outputPrinter.printMessage("Task updated successfully.") }
                        .onFailure { outputPrinter.printMessage("Failed to update task: ${it.message}") }

                    return
                }

                else -> outputPrinter.printMessage("Invalid option. Try again.")
            }
        }
    }

    private fun getLogsForTaskChanges(old: Task, new: Task): List<String> {
        val logs = mutableListOf<String>()

        if (old.title != new.title) {
            logs.add(createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = old.title,
                entityType = EntityType.TASK,
                username = old.createdBy,
                fieldName = "title",
                oldValue = old.title,
                newValue = new.title
            ))
        }

        if (old.description != new.description) {
            logs.add(createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = old.title,
                entityType = EntityType.TASK,
                username = old.createdBy,
                fieldName = "description",
                oldValue = old.description,
                newValue = new.description
            ))
        }

        if (old.taskState != new.taskState) {
            logs.add(createLogUseCase.invoke(
                operationType = OperationType.UPDATE,
                entityName = old.title,
                entityType = EntityType.TASK,
                username = old.createdBy,
                fieldName = "taskState",
                oldValue = old.taskState,
                newValue = new.taskState
            ))
        }

        return logs
    }
}