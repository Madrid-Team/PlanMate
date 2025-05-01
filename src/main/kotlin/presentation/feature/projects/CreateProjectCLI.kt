package presentation.feature.projects

import domain.usecases.project.CreateProjectUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateProjectCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val createProjectUseCase: CreateProjectUseCase
) {
    fun show() {
    }
}