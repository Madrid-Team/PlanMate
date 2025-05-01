package presentation.feature.projects

import domain.usecases.project.EditProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val editProjectUseCase: EditProjectUseCase
) {
    fun show() {
    }
}