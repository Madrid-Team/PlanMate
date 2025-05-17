package domain.usecases.task

import domain.models.task.Task
import domain.utils.TaskExceptions

class TaskValidatorUseCase {
    fun validateBasic(task: Task) {
        if (task.title.isBlank()) {
            throw TaskExceptions.TaskTitleIsEmptyException()
        }
        if (task.description.isBlank()) {
            throw TaskExceptions.TaskDescriptionIsEmptyException()
        }
    }

    fun validateAll(task: Task) {
        validateBasic(task)
        if (task.taskState.isBlank()) {
            throw TaskExceptions.TaskStateIsEmptyException()
        }
    }
}