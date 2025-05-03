package presentation.feature

import domain.usecases.task.DisplayAllTasksUseCase
import presentation.components.InputReader
import presentation.components.OutputPrinter

class TaskAuditLogCLI(
    private val inputReader: InputReader,
    private val outputPrinter: OutputPrinter,
    private val displayAllTasksUseCase: DisplayAllTasksUseCase
) {
    fun show() {
    }
}
