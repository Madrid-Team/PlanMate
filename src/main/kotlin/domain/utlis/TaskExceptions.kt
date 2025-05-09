package domain.utlis

open class TaskExceptions(message: String) : PlanMateExceptions(message) {
    class TaskNotFoundException(error: String? = "This task not found") : TaskExceptions(error ?: "This task not found")
    class TaskCannotDeleteException(error: String = "Can't delete this task") : TaskExceptions(error)
    class TaskTitleIsEmptyException(error: String = "Task title is empty") : TaskExceptions(error)
    class TaskCannotEditException(error: String = "Can't edit this task") : TaskExceptions(error)
    class NoLogsFoundException(error: String = "There are no logs found for this task") : TaskExceptions(error)
}