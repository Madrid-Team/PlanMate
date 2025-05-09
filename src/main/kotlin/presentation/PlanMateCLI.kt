package presentation

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.models.logs.CurrentUser
import presentation.feature.tasks.TaskCLI
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.admin.AdminCLI
import presentation.feature.projects.ProjectCLI
import presentation.feature.user.UserCLI

class PlanMateCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val authenticationCLI: AuthenticationCLI,
    private val taskCLI: TaskCLI,
    private val projectCLI: ProjectCLI,
    private val userCLI: UserCLI,
    private val adminCLI: AdminCLI
) {
    suspend fun start() {
        outputPrinter.printMessage("=== Welcome to PlanMate ===")

        while (true) {
            outputPrinter.printMessage("=== === Main Menu === ===")
            outputPrinter.printMessage("1. Log in")
            outputPrinter.printMessage("0. Exit")


            when (inputReader.readInput("Select an option: ")) {
                "1" -> {
                    authenticationCLI.login()
                    val user = CurrentUser.getCurrentUser()
                    if (user != null) {
                        showMenuForUser(user)
                    }
                    break
                }

                "0" -> {
                    outputPrinter.printMessage("Goodbye!")
                    return
                }

                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }

    private suspend fun showMenuForUser(user: User) {
        outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")

        when (user.role) {
            UserRole.ADMIN.name -> showAdminMenu()
            UserRole.MATE.name -> showMateMenu()
        }
    }

    private suspend fun showAdminMenu() {
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
                "4" -> adminCLI.showAdminMenu()
                "0" -> return
                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }

    private suspend fun showMateMenu() {
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
