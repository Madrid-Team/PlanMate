package presentation.feature.projects

import presentation.components.InputReader
import presentation.components.OutputPrinter

class ProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectCLI: CreateProjectCLI,
    private val deleteProjectCLI: DeleteProjectCLI,
    private val editProjectCLI: EditProjectCLI,
) {
    fun show() {
        while (true) {
            outputPrinter.printMessage("=== Project Menu ===")
            outputPrinter.printMessage("1. Create Project")
            outputPrinter.printMessage("2. Edit Project")
            outputPrinter.printMessage("3. Delete Project")
            outputPrinter.printMessage("0. Back")

            when (inputReader.readInput("Select an option:")) {
                "1" -> createProjectCLI.show()
                "2" -> editProjectCLI.show()
                "3" -> deleteProjectCLI.show()
                "0" -> return
                else -> outputPrinter.printMessage("Invalid option. Please try again.")
            }
        }
    }

}