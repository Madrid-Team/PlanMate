package presentation.feature.projects

import domain.models.logs.CurrentUser
import domain.models.project.Project
import domain.usecases.project.CreateProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectUseCase: CreateProjectUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Create Project ===")
        val name = inputReader.readInput("Enter project name: ")
        val description = inputReader.readInput("Enter project description: ")
        val taskStates = inputReader.readInput("Enter task States seperated by white space description: ")
        val projectStates = inputReader.readInput("Enter project States seperated by white space description: ")


        val currentProjectStates = projectStates.split(" ")
        val statesMenu = currentProjectStates.mapIndexed { index, state -> "${index + 1}. $state" }.joinToString("\n")
        val promptMessage = "Enter select project State:\n$statesMenu\nEnter number: "

        var projectState: String
        while (true) {
            val projectStateInput = inputReader.readInput(promptMessage)

            val selectedIndex = projectStateInput.toIntOrNull()?.minus(1)
            if (selectedIndex != null && selectedIndex in 0 until currentProjectStates.size) {
                projectState = currentProjectStates[selectedIndex]
                break
            } else {
                println("Invalid selection. Please enter a valid state id ")
                currentProjectStates.firstOrNull() ?: ""
            }
        }

        val project = Project(
            name = name,
            description = description,
            createdBy = CurrentUser.getCurrentUser()?.username ?: "UNKNOWN",
            projectLogs = emptyList(),
            projectState = projectState,
            taskStates = taskStates.trim().split(" "),
            projectStates = currentProjectStates,
            id = UUID.randomUUID()
        )
        val result = createProjectUseCase.createProject(project)

        result.onSuccess {
            outputPrinter.printMessage("Project created successfully")
        }.onFailure {
            outputPrinter.printMessage("Failed to create project: ${it.message}")
        }
    }
}