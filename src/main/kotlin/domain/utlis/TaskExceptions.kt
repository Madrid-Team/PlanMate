package domain.utlis

class TaskNameExistException(error: String = "This task name is already exists") : Exception(error)
class TaskNotFoundException(error: String? = "This task not found") : Exception(error)
class CannotCreateTaskException(error: String = "Can't create this task") : Exception(error)
class TaskTitleIsEmptyException(error: String = "Task title is empty") : Exception(error)
class TaskCannotEditException(error: String = "Can't edit this task") : Exception(error)