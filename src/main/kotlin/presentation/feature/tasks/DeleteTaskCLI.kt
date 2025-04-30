package presentation.feature.tasks

import domain.usecases.DeleteTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class DeleteTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val useCase: DeleteTaskUseCase
) {
    fun show() {
    }
}