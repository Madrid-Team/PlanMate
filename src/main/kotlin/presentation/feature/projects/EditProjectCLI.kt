package presentation.feature.projects

import domain.usecases.EditProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val useCase: EditProjectUseCase,
) {
    fun show() {
    }
}