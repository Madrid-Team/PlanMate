package domain.usecases.task.validation

import domain.utlis.TaskTitleInvalidException
import domain.utlis.TaskTitleIsEmptyException

class CheckTaskTitleValidationUseCase {
    operator fun invoke(taskTitle: String) {
        if (taskTitle.isEmpty()) {
            throw TaskTitleIsEmptyException()
        } else if (!taskTitle.matches(Regex("^[A-Za-z ]+$"))) {
            throw TaskTitleInvalidException()
        }
    }
}