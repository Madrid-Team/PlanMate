package presentation.feature.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class UserCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createUserCLI: CreateUserCLI,
    private val deleteUserCLI: DeleteUserCLI
) {
    suspend fun show() = withContext(Dispatchers.IO) {
        while (true) {
            outputPrinter.printMenuItems(
                listOf(
                    String.manageUsersMenu,
                    String.createNewUser,
                    String.deleteUser,
                    String.back
                )
            )
            when (inputReader.readInput(String.selectOption)) {
                String.selectionOne -> createUserCLI.show()
                String.selectionTwo -> deleteUserCLI.show()
                String.selectionZero -> return@withContext
                else -> outputPrinter.printError(String.invalidOption)
            }
        }
    }
}