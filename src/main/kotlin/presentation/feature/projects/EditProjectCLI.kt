package presentation.feature.projects

import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.usecases.logs.CreateLogUseCase
import domain.usecases.project.EditProjectUseCase
import domain.usecases.project.GetProjectByIdUseCase
import domain.utlis.PlanMateExceptions
import domain.utlis.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editProjectUseCase: EditProjectUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val createLogUseCase: CreateLogUseCase
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Edit Project ===")
        val id = inputReader.readInput("Enter the ID of the project to edit:")
        try {
            val currentProject = getProjectByIdUseCase.invoke(id)
            var updatedProject = currentProject.copy()

            while (true) {
                outputPrinter.printMessage("Choose fields to edit")
                outputPrinter.printMessage("1 - Name")
                outputPrinter.printMessage("2 - Description")
                outputPrinter.printMessage("3 - Project States")
                outputPrinter.printMessage("4 - Finish")

                when (inputReader.readInput("Select an option:")) {
                    "1" -> {
                        val newName = inputReader.readInput("Enter the new name:")
                        updatedProject = updatedProject.copy(name = newName)
                    }

                    "2" -> {
                        val newDescription = inputReader.readInput("Enter the new description:")
                        updatedProject = updatedProject.copy(description = newDescription)
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
                                updatedProject =
                                    updatedProject.copy(projectState = currentProject.projectStates[selectedIndex])
                                break
                            } else {
                                println("Invalid selection. Please enter a valid state id ")
                            }
                        }

                    }

                    "4" -> {
                        //no updates entered
                        if (currentProject == updatedProject) {
                            break
                        } else {
                            try {
                                editProjectUseCase(
                                    updatedProject.copy(
                                        projectLogs = currentProject.projectLogs + getLogsForAllChangesInUpdatedProject(
                                            currentProject,
                                            updatedProject
                                        )
                                    )
                                )
                                outputPrinter.printMessage("Project edited successfully.")
                                break
                            } catch (e: PlanMateExceptions) {
                                outputPrinter.printMessage("Failed to edit project: ${e.message}")
                            }
                        }
                    }

                    else -> outputPrinter.printMessage("Invalid option. Please try again.")
                }
            }
        } catch (e: ProjectExceptions.ProjectNotFoundException) {
            outputPrinter.printMessage(e.message ?: "Project not found")
        }
    }

    private fun getLogsForAllChangesInUpdatedProject(currentProject: Project, updatedProject: Project): List<String> {
        val listOfLogs = mutableListOf<String>()
        if (updatedProject.name != currentProject.name) {
            listOfLogs.add(
                createLogUseCase.invoke(
                    operationType = OperationType.UPDATE,
                    entityName = currentProject.name,
                    entityType = EntityType.PROJECT,
                    username = currentProject.createdBy,
                    fieldName = "name",
                    oldValue = currentProject.name,
                    newValue = updatedProject.name,
                )
            )
        }

        if (updatedProject.description != currentProject.description) {
            listOfLogs.add(
                createLogUseCase.invoke(
                    operationType = OperationType.UPDATE,
                    entityName = currentProject.name,
                    entityType = EntityType.PROJECT,
                    username = currentProject.createdBy,
                    fieldName = "description",
                    oldValue = currentProject.description,
                    newValue = updatedProject.description,
                )
            )
        }
        if (updatedProject.projectState != currentProject.projectState) {
            listOfLogs.add(
                createLogUseCase.invoke(
                    operationType = OperationType.UPDATE,
                    entityName = currentProject.name,
                    entityType = EntityType.PROJECT,
                    username = currentProject.createdBy,
                    fieldName = "project state",
                    oldValue = currentProject.projectState,
                    newValue = updatedProject.projectState,
                )
            )
        }
        return listOfLogs
    }
}