package presentation.feature.user

import domain.usecases.CreateUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateUserCLI(
    private val createUserUseCase: CreateUserUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter
) {
    fun show() {
    }
    fun showCreatedUser() {

    }
}