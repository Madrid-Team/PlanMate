package domain.usecases.task.validation

import domain.utlis.TaskDescriptionIsEmptyException

class CheckTaskDescriptionUseCase {
    operator fun invoke(taskDescription: String) {
        if (taskDescription.isEmpty()) {
            throw TaskDescriptionIsEmptyException()
        }
    }
}