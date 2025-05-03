package domain.usecases.task.validation

import domain.utlis.TaskStateIsEmptyException

class CheckTaskStateUseCase {
    operator fun invoke(taskState: String) {
        if (taskState.isEmpty()) {
            throw TaskStateIsEmptyException()
        }
    }
}