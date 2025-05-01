package presentation.feature.user

import domain.usecases.DeleteUser
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteUserCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteUserUseCase: DeleteUser
) {
    fun show() {
    }
}