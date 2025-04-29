package domain.utlis

class TaskNameExistException(error: String = "This task name is already exists") : Exception(error)
class TaskNotFoundException(error: String = "This task not found") : Exception(error)