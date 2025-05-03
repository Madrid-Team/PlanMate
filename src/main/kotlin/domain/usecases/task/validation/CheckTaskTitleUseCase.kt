package domain.usecases.task.validation

import domain.utlis.TaskTitleIsEmptyException

class CheckTaskTitleUseCase {
    operator fun invoke(taskTitle: String) {
        if (taskTitle.isEmpty()) {
            throw TaskTitleIsEmptyException()
        }
    }
}