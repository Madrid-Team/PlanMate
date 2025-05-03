package domain.usecases.task.validation

import domain.utlis.TaskDescriptionIsEmptyException

class CheckTaskDescriptionValidationUseCase {
    operator fun invoke(taskDescription: String) {
        if (taskDescription.isEmpty()) {
            throw TaskDescriptionIsEmptyException()
        }
    }
}