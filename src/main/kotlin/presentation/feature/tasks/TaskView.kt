package presentation.feature.tasks

import domain.usecases.DisplayAllTasksUseCase
import presentation.components.OutputPrinter


class TaskView (private val displayAllTasksUseCase: DisplayAllTasksUseCase,
                private val outputPrinter: OutputPrinter
){

    fun show() {}
}