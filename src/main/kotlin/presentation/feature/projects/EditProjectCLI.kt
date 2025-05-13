package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.EditProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editProjectUseCase: EditProjectUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    var hasChanges = false
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage(String.editProjectHeader)
        val id = inputReader.readInput(String.enterProjectIdToEdit)
        try {
            val currentProject = getProjectByIdUseCase.getById(id)
            hasChanges = false

            while (true) {
                outputPrinter.printMessage(String.chooseFieldsEditProject)

                when (inputReader.readInput(String.selectOption)) {
                    String.selectionOne -> editProjectName(currentProject)
                    String.selectionTwo -> editProjectDescription(currentProject)
                    String.selectionThree -> editProjectStates(currentProject)
                    String.selectionFour -> {
                        editProjectFinished()
                        break
                    }

                    else -> outputPrinter.printMessage("Invalid option. Please try again.")
                }
            }
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage(exception.message ?: "Error while editing project")
        }
    }

    suspend fun editProjectName(currentProject: Project) {
        val newName = inputReader.readInput("Enter the new name:")
        try {
            val newProject = currentProject.copy(name = newName)
            editProjectUseCase.editProject(newProject)
            outputPrinter.printMessage("Project name updated successfully.")
            hasChanges = true
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage("Failed to update project description: ${exception.message}")
        }
    }

    suspend fun editProjectDescription(currentProject: Project) {
        val newDescription = inputReader.readInput("Enter the new description:")
        try {
            val newDescription = currentProject.copy(description = newDescription)
            editProjectUseCase.editProject(newDescription)
            outputPrinter.printMessage("Project description updated successfully.")
            hasChanges = true
        } catch (exception: ProjectExceptions) {
            outputPrinter.printMessage("Failed to update project description: ${exception.message}")
        }
    }

    suspend fun editProjectStates(currentProject: Project) {
        val taskStatesMenu =
            currentProject.projectStates.mapIndexed { index, state -> "${index + 1}. $state" }
                .joinToString("\n")
        val promptMessage = "Select project State:\n$taskStatesMenu\nEnter number: "

        while (true) {
            val projectStateInput = inputReader.readInput(promptMessage)

            val selectedIndex = projectStateInput.toIntOrNull()?.minus(1)
            if (selectedIndex != null && selectedIndex in 0 until currentProject.projectStates.size) {
                val newState = currentProject.projectStates[selectedIndex]
                try {
                    val newProject = currentProject.copy(projectState = newState)
                    editProjectUseCase.editProject(newProject)
                    outputPrinter.printMessage("Project state updated successfully.")
                    hasChanges = true
                } catch (e: ProjectExceptions) {
                    outputPrinter.printMessage("Failed to update project state: ${e.message}")
                }
                break
            } else {
                println("Invalid selection. Please enter a valid state id ")
            }
        }
    }

    private fun editProjectFinished() {
        if (hasChanges) {
            outputPrinter.printMessage("Project edited successfully.")
        } else {
            outputPrinter.printMessage("No changes were made to the project.")
        }
    }
}