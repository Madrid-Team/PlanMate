package domain.usecases.task.validation

import domain.utlis.ProjectIdIsEmptyException

class CheckTaskStateUseCase {
    operator fun invoke(taskState: String) {
        if (taskState.isEmpty()) {
            throw ProjectIdIsEmptyException()
        }
    }
}