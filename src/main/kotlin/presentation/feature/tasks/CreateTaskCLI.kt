package presentation.feature.tasks

import domain.usecases.CreateTaskUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class CreateTaskCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val taskView: TaskView,
    private val createTaskUseCase: CreateTaskUseCase
) {
    fun show() {
    }
}