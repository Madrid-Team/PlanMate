package presentation.feature.user

import data.source.user.CurrentUserProvider
import domain.usecases.user.DeleteUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.utils.*

class DeleteUserCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val currentUserProvider: CurrentUserProvider
) {
    suspend fun show() {
        outputPrinter.printMenuItems(listOf(String.deleteUserStarted, String.enterUserId))
        val userId = inputReader.readInput()
        try {
            val requiredId = currentUserProvider.getCurrentUser().id
            deleteUserUseCase.deleteUser(requiredId.toString(), userId)
            outputPrinter.printMessage(String.deletedSuccess)
        } catch (e: Exception) {
            outputPrinter.printMenuItems(listOf(String.deletedFailed, e.message.toString(), String.pressOneToTryAgain))
            val userOption = inputReader.readInput()
            if (userOption == String.selectionOne) show()
        }
    }
}