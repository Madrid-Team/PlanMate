package presentation.feature.user

import domain.usecases.CreateUserUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateUserCLI(
    private val useCase: CreateUserUseCase,
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter
) {
    fun show() {
    }
}