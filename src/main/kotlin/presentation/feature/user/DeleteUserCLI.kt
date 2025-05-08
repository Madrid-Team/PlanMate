package presentation.feature.user

import domain.models.logs.CurrentUser
import domain.usecases.user.DeleteUserUseCase
import kotlinx.coroutines.runBlocking
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteUserCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteUserUseCase: DeleteUserUseCase
) {
    fun show() {
        outputPrinter.printMessage("=== Delete user started ===")
        outputPrinter.printMessage("Enter user id:")
        val userId = inputReader.readInput()
        try {
            val requiredId = CurrentUser.getCurrentUser()?.id
            runBlocking { deleteUserUseCase.invoke(requiredId.toString(), userId) }
            outputPrinter.printMessage("Deleted Success")
        } catch (_: Exception) {
            outputPrinter.printMessage("Deleted Failed")
            outputPrinter.printMessage("if you want to try again enter \"1\" else enter anything")
            val userOption = inputReader.readInput()
            if (userOption == "1") show()
        }
    }
}