package presentation.feature.projects

import domain.usecases.project.EditProjectDescriptionUseCase
import domain.usecases.project.EditProjectNameUseCase
import domain.usecases.project.EditProjectStateUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editProjectNameUseCase: EditProjectNameUseCase,
    private val editProjectDescriptionUseCase: EditProjectDescriptionUseCase,
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Edit Project ===")
        val id = inputReader.readInput("Enter the ID of the project to edit:")
        try {
            val currentProject = getProjectByIdUseCase.getById(id)
            var hasChanges = false

            while (true) {
                outputPrinter.printMessage("Choose fields to edit")
                outputPrinter.printMessage("1 - Name")
                outputPrinter.printMessage("2 - Description")
                outputPrinter.printMessage("3 - Project States")
                outputPrinter.printMessage("4 - Finish")

                when (inputReader.readInput("Select an option:")) {
                    "1" -> {
                        val newName = inputReader.readInput("Enter the new name:")
                        try {
                            editProjectNameUseCase.execute(currentProject.id, newName)
                            outputPrinter.printMessage("Project name updated successfully.")
                            hasChanges = true
                        } catch (e: PlanMateExceptions) {
                            outputPrinter.printMessage("Failed to update project name: ${e.message}")
                        }
                    }

                    "2" -> {
                        val newDescription = inputReader.readInput("Enter the new description:")
                        try {
                            editProjectDescriptionUseCase.execute(currentProject.id, newDescription)
                            outputPrinter.printMessage("Project description updated successfully.")
                            hasChanges = true
                        } catch (e: PlanMateExceptions) {
                            outputPrinter.printMessage("Failed to update project description: ${e.message}")
                        }
                    }

                    "3" -> {
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
                                    editProjectStateUseCase.execute(currentProject.id, newState)
                                    outputPrinter.printMessage("Project state updated successfully.")
                                    hasChanges = true
                                } catch (e: PlanMateExceptions) {
                                    outputPrinter.printMessage("Failed to update project state: ${e.message}")
                                }
                                break
                            } else {
                                println("Invalid selection. Please enter a valid state id ")
                            }
                        }
                    }

                    "4" -> {
                        if (hasChanges) {
                            outputPrinter.printMessage("Project edited successfully.")
                        } else {
                            outputPrinter.printMessage("No changes were made to the project.")
                        }
                        break
                    }

                    else -> outputPrinter.printMessage("Invalid option. Please try again.")
                }
            }
        } catch (e: ProjectExceptions.ProjectNotFoundException) {
            outputPrinter.printMessage(e.message ?: "Project not found")
        }catch (e: ProjectExceptions.NoChangesException){
            outputPrinter.printMessage(e.message ?: "No changes were made to the project")
        }
    }
}