package presentation.feature.projects

import domain.models.project.Project
import domain.usecases.project.CreateProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectUseCase: CreateProjectUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Create Project ===")
        val name = inputReader.readInput("Enter project name: ")
        val description = inputReader.readInput("Enter project description: ")

        val project = Project(
            name = name,
            description = description,
            createdBy = "",
            projectLogs = emptyList(),
            projectState = "",
            taskStates = emptyList(),
            projectStates = listOf()
        )
        val result = createProjectUseCase.createProject(project)

        result.onSuccess {
            outputPrinter.printMessage("Project created successfully")
        }.onFailure {
            outputPrinter.printMessage("Failed to create project: ${it.message}")
        }
    }
}