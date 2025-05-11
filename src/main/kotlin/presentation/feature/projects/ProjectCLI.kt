package presentation.feature.projects

import domain.usecases.project.GetAllProjectsUseCase
import domain.utils.PlanMateExceptions
import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectCLI: CreateProjectCLI,
    private val deleteProjectCLI: DeleteProjectCLI,
    private val editProjectCLI: EditProjectCLI,
    private val projectAuditLogCLI: ProjectAuditLogCLI,
    private val projectView: ProjectView,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) {
    suspend fun show() {
        while (true) {
            outputPrinter.printMessage("=== Project Menu ===")
            outputPrinter.printMessage("1. Show Projects")
            outputPrinter.printMessage("2. Create Project")
            outputPrinter.printMessage("3. Edit Project")
            outputPrinter.printMessage("4. Delete Project")
            outputPrinter.printMessage("5. Show Project Logs By ID")
            outputPrinter.printMessage("0. Back")

            when (inputReader.readInput("Select an option:")) {
                "1" -> showProjects()
                "2" -> createProjectCLI.show()
                "3" -> editProjectCLI.show()
                "4" -> deleteProjectCLI.show()
                "5" -> projectAuditLogCLI.show()
                "0" -> return
                else -> outputPrinter.printMessage("Invalid option. Please try again.")
            }
        }
    }

    suspend fun showProjects() {
        try {
            getAllProjectsUseCase().also {
                projectView.projectList(it)
            }
        } catch (e: PlanMateExceptions) {
            outputPrinter.printMessage("Failed to load projects:${e.message}")
        }
    }

}