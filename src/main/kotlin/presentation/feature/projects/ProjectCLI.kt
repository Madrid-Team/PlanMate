package presentation.feature.projects

import domain.usecases.project.GetAllProjectsUseCase
import domain.utils.PlanMateExceptions
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

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
            outputPrinter.printMenuItems(listOf(
                String.projectMenuHeader,
                String.displayProjects,
                String.createProject,
                String.editProject,
                String.deleteProject,
                String.displayProjectLogs,
                String.back
            ))

            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> showProjects()
                String.selectionTwo -> createProjectCLI.show()
                String.selectionThree -> editProjectCLI.show()
                String.selectionFour -> deleteProjectCLI.show()
                String.selectionFive -> projectAuditLogCLI.show()
                String.selectionZero -> return
                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }
    suspend fun showProjects() {
        outputPrinter.printMessage(String.displayProjects)
        try {
            getAllProjectsUseCase.execute().also {
                projectView.projectList(it)
            }
        } catch (e: PlanMateExceptions) {
            outputPrinter.printError("${String.failedLoadProjects} ${e.message}")
        }
    }

}