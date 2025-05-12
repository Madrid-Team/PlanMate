package presentation.feature.admin

import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.ProjectAuditLogCLI
import presentation.feature.projects.ProjectCLI
import presentation.feature.tasks.TaskAuditLogCLI
import presentation.feature.tasks.TaskCLI
import presentation.feature.user.UserCLI

class AdminCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val projectAuditLogCLI: ProjectAuditLogCLI,
    private val taskAuditLogCLI: TaskAuditLogCLI,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
    private val userCLI: UserCLI,
) {

    suspend fun showAdminMenu() {
        while (true) {
            outputPrinter.printMessage("=== Admin Menu ===")
            outputPrinter.printMessage("1. Manage tasks")
            outputPrinter.printMessage("2. Manage projects")
            outputPrinter.printMessage("3. Manage users")
            outputPrinter.printMessage("4. Admin tools")
            outputPrinter.printMessage("0. Log out")

            when (inputReader.readInput("Select an option: ")) {
                "1" -> taskCLI.show()
                "2" -> projectCLI.show()
                "3" -> userCLI.show()
                "4" -> showAdminMenuTools()
                "0" -> return
                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }

    suspend fun showAdminMenuTools() {
        while (true) {
            println("=== Admin Tools ===")
            println("1. View Project Logs")
            println("2. View Task Logs")
            println("0. Back")
            print("Choose an option: ")

            when (readln()) {
                "1" -> projectAuditLogCLI.show()
                "2" -> taskAuditLogCLI.show()
                "0" -> return
                else -> println("Invalid option!")
            }
        }
    }
}