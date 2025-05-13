package presentation

import domain.models.authentication.User
import domain.models.authentication.UserRole
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.AuthenticationCLI
import presentation.feature.admin.AdminCLI
import presentation.feature.mate.MateCLI
import presentation.utils.*

class PlanMateCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val authenticationCLI: AuthenticationCLI,
    private val adminCLI: AdminCLI,
    private val mateCLI: MateCLI,
 ) {
    suspend fun start() {
        outputPrinter.printMessage(String.welcomeMessage)

        while (true) {
            outputPrinter.printMessage(String.mainMenu)
            outputPrinter.printMessage(String.login)
            outputPrinter.printMessage(String.exit)


            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> {
                     showMenuForUser(authenticationCLI.login())
                    break
                }

                String.selectionZero -> {
                    outputPrinter.printMessage(String.goodbye)
                    return
                }

                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }

    private suspend fun showMenuForUser(user: User) {
        outputPrinter.printMessage("\nWelcome, ${user.username}! (Role: ${user.role})")

        when (user.role) {
            UserRole.ADMIN.name -> adminCLI.showAdminMenu()
            UserRole.MATE.name -> mateCLI.showMateMenu()
        }
    }

}
