package domain.usecases.task

import domain.models.task.Task
import domain.utils.TaskDescriptionIsEmptyException
import domain.utils.TaskExceptions
import domain.utils.TaskStateIsEmptyException
import domain.utils.TaskTitleIsEmptyException

class TaskValidator {
    fun validateBasic(task: Task) {
        if (task.title.isBlank()) {
            throw  TaskTitleIsEmptyException()
        }
        if (task.description.isBlank()) {
            throw TaskDescriptionIsEmptyException()
        }
    }

    fun validateAll(task: Task) {
        validateBasic(task)
        if (task.taskState.isBlank()) {
            throw TaskStateIsEmptyException()
        }
    }
}