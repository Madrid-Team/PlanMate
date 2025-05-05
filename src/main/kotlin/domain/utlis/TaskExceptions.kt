package domain.utlis

open class TaskExceptions(message: String) : PlanMateExceptions(message) {
  //  class TaskNameExistException(error: String = "This task name is already exists") : TaskExceptions(error)
    class TaskNotFoundException(error: String? = "This task not found") : TaskExceptions(error ?: "This task not found")
    class TaskCannotCreateException(error: String = "Can't create this task") : TaskExceptions(error)
    class TaskCannotDeleteException(error: String = "Can't delete this task") : TaskExceptions(error)
    class TaskTitleIsEmptyException(error: String = "Task title is empty") : TaskExceptions(error)
    class TaskCannotEditException(error: String = "Can't edit this task") : TaskExceptions(error)
    class NoLogsFoundException(error: String = "There are no logs found for this task") : TaskExceptions(error)
}