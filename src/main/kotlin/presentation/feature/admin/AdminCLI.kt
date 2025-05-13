package presentation.feature.admin

import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskCLI
import presentation.feature.user.UserCLI
import presentation.utils.*

class AdminCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
    private val userCLI: UserCLI,
) {

    suspend fun showAdminMenu() {
        while (true) {
            outputPrinter.printMenuItems(
                listOf(
                    String.adminMenu,
                    String.manageTasks,
                    String.manageProjects,
                    String.manageUsers,
                    String.logout
                )
            )
            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> taskCLI.show()
                String.selectionTwo -> projectCLI.show()
                String.selectionThree -> userCLI.show()
                String.selectionZero -> return
                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }
}