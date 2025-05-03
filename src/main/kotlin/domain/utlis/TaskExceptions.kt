package domain.utlis

class TaskNameExistException(error: String = "This task name is already exists") : Exception(error)
class TaskNotFoundException(error: String? = "This task not found") : Exception(error)
class CannotCreateTaskException(error: String = "Can't create this task") : Exception(error)
class TaskCannotEditException(error: String = "Can't edit this task") : Exception(error)
class TaskCannotDeleteException(error: String = "Can't delete this task") : Exception(error)
class NoLogsFoundException(error: String = "There are no logs found for this task") : Exception(error)

class ProjectIdIsEmptyException(error: String = "Project Id is Empty") : Exception(error)
class TaskTitleIsEmptyException(error: String = "Task title is Empty") : Exception(error)
class TaskDescriptionIsEmptyException(error: String = "Task description is Empty") : Exception(error)
class TaskStateIsEmptyException(error: String = "Task state is Empty") : Exception(error)
