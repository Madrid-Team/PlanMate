package presentation.feature.tasks

import domain.usecases.EditTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val editTaskUseCase: EditTaskUseCase
) {
    fun show() {
    }
}