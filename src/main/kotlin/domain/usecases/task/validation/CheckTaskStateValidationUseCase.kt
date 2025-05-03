package domain.usecases.task.validation

import domain.utlis.TaskStateIsEmptyException

class CheckTaskStateValidationUseCase {
    operator fun invoke(taskState: String) {
        if (taskState.isEmpty()) {
            throw TaskStateIsEmptyException()
        }
    }
}