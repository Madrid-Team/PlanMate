package presentation.feature.mate

import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI
import presentation.utils.*

class MateCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
) {
    suspend fun showMateMenu() {
        while (true) {
            outputPrinter.printMenuItems(
                listOf(
                    String.mateMenu,
                    String.viewTaskMenu,
                    String.viewProjects,
                    String.logout
                )
            )

            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> taskCLI.show()
                String.selectionTwo -> projectCLI.showProjects()
                String.selectionZero -> return
                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }
}