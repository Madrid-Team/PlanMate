package presentation.feature.projects

import domain.usecases.project.GetAllProjectsUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectCLI: CreateProjectCLI,
    private val deleteProjectCLI: DeleteProjectCLI,
    private val editProjectCLI: EditProjectCLI,
    private val projectView: ProjectView,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) {
    fun show() {
        while (true) {
            outputPrinter.printMessage("=== Project Menu ===")
            outputPrinter.printMessage("1. Show Projects")
            outputPrinter.printMessage("2. Edit Project")
            outputPrinter.printMessage("3. Delete Project")
            outputPrinter.printMessage("4. Create Project")
            outputPrinter.printMessage("0. Back")

            when (inputReader.readInput("Select an option:")) {
                "1" -> showProjects()
                "2" -> createProjectCLI.show()
                "3" -> editProjectCLI.show()
                "4" -> deleteProjectCLI.show()
                "0" -> return
                else -> outputPrinter.printMessage("Invalid option. Please try again.")
            }
        }
    }

    fun showProjects() {
        getAllProjectsUseCase.getAllProjects().onSuccess {
            projectView.projectList(it)
        }.onFailure { outputPrinter.printMessage("Failed to load projects:${it.message}")}
    }

}