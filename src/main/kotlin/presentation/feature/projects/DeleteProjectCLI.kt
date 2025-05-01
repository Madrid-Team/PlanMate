package presentation.feature.projects

import domain.usecases.project.DeleteProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val deleteProjectUseCase: DeleteProjectUseCase
) {
    fun show() {
    }
}