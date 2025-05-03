package presentation.feature.tasks

import domain.usecases.DisplayAllTasksUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter


class TaskView(
    private val displayAllTasksUseCase: DisplayAllTasksUseCase,
    private val outputPrinter: OutputPrinter,
    private val inputReader: InputReader
) {

    fun show() {}
}