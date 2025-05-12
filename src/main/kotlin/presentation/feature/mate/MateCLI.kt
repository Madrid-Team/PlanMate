package presentation.feature.mate

import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI

class MateCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
    ) {
    suspend fun showMateMenu() {
        while (true) {
            outputPrinter.printMessage("=== Mate Menu ===")
            outputPrinter.printMessage("1. View my tasks")
            outputPrinter.printMessage("2. View projects")
            outputPrinter.printMessage("0. Log out")

            when (inputReader.readInput("Select an option: ")) {
                "1" -> taskCLI.show()
                "2" -> projectCLI.showProjects()
                "0" -> return
                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }
}