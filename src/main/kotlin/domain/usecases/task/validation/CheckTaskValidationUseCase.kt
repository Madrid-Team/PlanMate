package domain.usecases.task.validation

import domain.models.task.Task

class CheckTaskValidationUseCase(
    private val checkProjectIdValidationUseCase: CheckProjectIdValidationUseCase,
    private val checkTaskDescriptionValidationUseCase: CheckTaskDescriptionValidationUseCase,
    private val checkTaskStateValidationUseCase: CheckTaskStateValidationUseCase,
    private val checkTaskTitleValidationUseCase: CheckTaskTitleValidationUseCase
) {
    operator fun invoke(task: Task) {
        checkProjectIdValidationUseCase(task.projectId)
        checkTaskDescriptionValidationUseCase(task.description)
        checkTaskStateValidationUseCase(task.taskState)
        checkTaskTitleValidationUseCase(task.title)
    }
}