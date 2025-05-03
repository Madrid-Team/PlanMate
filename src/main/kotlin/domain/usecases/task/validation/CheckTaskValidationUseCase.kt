package domain.usecases.task.validation

import domain.models.task.Task

class CheckTaskValidationUseCase(
    private val checkProjectIdUseCase: CheckProjectIdUseCase,
    private val checkTaskDescriptionUseCase: CheckTaskDescriptionUseCase,
    private val checkTaskStateUseCase: CheckTaskStateUseCase,
    private val checkTaskTitleUseCase: CheckTaskTitleUseCase
) {
    operator fun invoke(task: Task) {
        checkProjectIdUseCase(task.projectId)
        checkTaskDescriptionUseCase(task.description)
        checkTaskStateUseCase(task.taskState)
        checkTaskTitleUseCase(task.title)
    }
}