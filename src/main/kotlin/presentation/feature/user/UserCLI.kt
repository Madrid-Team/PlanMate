package presentation.feature.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter

class UserCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createUserCLI: CreateUserCLI,
    private val deleteUserCLI: DeleteUserCLI
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        while (true) {
            outputPrinter.printMessage("=== Manage users ===")
            outputPrinter.printMessage("1. Create new user")
            outputPrinter.printMessage("2. Delete new user")
            outputPrinter.printMessage("0. Back")
            when (inputReader.readInput("Select an option: ")) {
                "1" -> createUserCLI.show()
                "2" -> deleteUserCLI.show()
                "0" -> return@withContext
                else -> outputPrinter.printError("Invalid option.")
            }
        }
    }

}