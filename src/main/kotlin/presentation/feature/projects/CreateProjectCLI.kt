package presentation.feature.projects

import domain.models.logs.AuditLog
import domain.models.logs.CurrentUser
import domain.models.logs.EntityType
import domain.models.logs.OperationType
import domain.models.project.Project
import domain.usecases.project.CreateProjectUseCase
import domain.utils.PlanMateExceptions
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
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        outputPrinter.printMessage("=== Create Project ===")
        val name = inputReader.readInput("Enter project name: ")
        val description = inputReader.readInput("Enter project description: ")
        val taskStates = inputReader.readInput("Enter task States separated by white space description: ")
        val projectStates = inputReader.readInput("Enter project States separated by white space description: ")


        val currentProjectStates = projectStates.split(" ")
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
                currentProjectStates.firstOrNull() ?: ""
            }
        }

        val project = Project(
            name = name,
            description = description,
            createdBy = CurrentUser.getCurrentUser().username,
            projectLogs = emptyList(),
            projectState = projectState,
            taskStates = taskStates.trim().split(" "),
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
            val projectCreation = async {
                createProjectUseCase.execute(projectWithLog)
            }
            projectCreation.await()
            outputPrinter.printMessage("Project created successfully")
        } catch (e: PlanMateExceptions) {
            outputPrinter.printMessage("Failed to create project: ${e.message}")
        }
    }
}