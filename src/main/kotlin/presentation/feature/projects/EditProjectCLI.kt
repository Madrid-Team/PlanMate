package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.EditProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editProjectUseCase: EditProjectUseCase
) {
    fun show() {
        fun show() {
            outputPrinter.printMessage("=== Edit Project ===")
            val id = inputReader.readInput("Enter the ID of the project to edit:")
            val newName = inputReader.readInput("Enter the new name:")
            val newDescription = inputReader.readInput("Enter the new description:")

            val updatedProject = Project(
                id = id,
                name = newName,
                description = newDescription,
                createdBy = "",
                projectLogs = emptyList(),
                projectState = "",
                taskStates = emptyList(),
                projectStates = emptyList()
            )

            val result = editProjectUseCase.editProject(updatedProject)
            result.onSuccess { outputPrinter.printMessage("Project edited successfully.") }
            result.onFailure { outputPrinter.printMessage("Failed to edit project: ${result.exceptionOrNull()?.message}") }
        }

    }
}