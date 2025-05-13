package presentation.feature.projects

import data.source.user.CurrentUserProvider
import domain.models.logs.AuditLog
import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.usecases.project.CreateProjectUseCase
import domain.utils.PlanMateExceptions
import domain.utils.ProjectExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import java.util.*

class CreateProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectUseCase: CreateProjectUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Create Project ===")
        val name = inputReader.readInput("Enter project name: ")
        val description = inputReader.readInput("Enter project description: ")
        val taskStates = inputReader.readInput("Enter task States separated by (-): ")
        val projectStates = inputReader.readInput("Enter project States separated by (-): ")


        val currentProjectStates = projectStates.split("-").filter { it.isNotBlank() }
        val statesMenu =
            currentProjectStates.mapIndexed { index, state -> "${index + 1}. $state" }.joinToString("\n")
        val promptMessage = "Select project State:\n$statesMenu\nEnter number: "

        val projectState: String
        while (true) {
            val projectStateInput = inputReader.readInput(promptMessage)

            val selectedIndex = projectStateInput.toIntOrNull()?.minus(1)
            if (selectedIndex != null && selectedIndex in currentProjectStates.indices) {
                projectState = currentProjectStates[selectedIndex]
                break
            } else {
                println("Invalid selection. Please enter a valid state id ")
            }
        }

        val project = Project(
            name = name,
            description = description,
            createdBy = currentUserProvider.getCurrentUser().username,
            projectLogs = emptyList(),
            projectState = projectState,
            taskStates = taskStates.trim().split("-").filter { it.isNotBlank() },
            projectStates = currentProjectStates,
            id = UUID.randomUUID()
        )
        try {
            val logUseCase = async {
                    AuditLog(
                    operationType = OperationType.CREATE,
                    entityName = project.name,
                    entityType = EntityType.PROJECT,
                    username = project.createdBy,
                 ).toString()
            }
            val projectWithLog = project.copy(projectLogs = listOf(logUseCase.await()))

          createProjectUseCase.createProject(projectWithLog)

            outputPrinter.printMessage("Project created successfully")
        } catch (e: ProjectExceptions) {
            outputPrinter.printMessage("Failed to create project: ${e.message}")
        }
    }
}