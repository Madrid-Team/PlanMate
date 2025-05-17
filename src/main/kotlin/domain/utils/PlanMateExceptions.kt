package domain.utils

open class PlanMateExceptions(message: String) : Exception(message)

open class ProjectExceptions(message: String) : PlanMateExceptions(message) {
    class ProjectNotFoundException(message: String = "Project not found") : ProjectExceptions(message)
    class ProjectNameInvalidException : ProjectExceptions("Project name invalid,please enter a valid name")
    class ProjectStatesIsEmptyException(error: String = "Project States is empty") : ProjectExceptions(error)
    class ProjectDescriptionIsEmptyException(error: String = "Project description is empty") : ProjectExceptions(error)
    class ProjectTaskStatesIsEmptyException(error: String = "Project task states is empty") : ProjectExceptions(error)
    class ProjectsFileNotExistsException : ProjectExceptions("projects.csv not found")
    class ProjectsReadWriteException : ProjectExceptions("Couldn't access projects.csv")
    class NoLogsFoundException(error: String = "There are no logs found for this task") : ProjectExceptions(error)
    class ProjectDescriptionInvalidException :
        ProjectExceptions("Project description invalid, please enter a valid description")

    class ProjectDescriptionTooShortException : ProjectExceptions("Project description is too short")
}


open class TaskExceptions(message: String) : PlanMateExceptions(message) {
    class TaskNotFoundException(error: String? = "This task not found") : TaskExceptions(error ?: "This task not found")
    class TaskCannotDeleteException(error: String = "Can't delete this task") : TaskExceptions(error)
    class TaskTitleIsEmptyException(error: String = "Task title is empty") : TaskExceptions(error)
    class TaskDescriptionIsEmptyException(error: String = "Task description is empty") : TaskExceptions(error)
    class TaskStateIsEmptyException(error: String = "Task State is empty") : TaskExceptions(error)
    class TaskCannotEditException(error: String = "Can't edit this task") : TaskExceptions(error)
    class NoLogsFoundException(error: String = "There are no logs found for this task") : TaskExceptions(error)
}


open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class UserReadWriteException(message: String = "User read write error") : UserExceptions(message)
    class InvalidUserName() : UserExceptions(message = "Invalid username")
    class UserExist(message: String = "User already Exist") : UserExceptions(message)
    class UserNotAdminException : UserExceptions("User is not admin")
    class UserNotFoundException(message: String = "User not Found") : UserExceptions(message)
    class EmptyPasswordException(message: String = "Password Cannot be empty") : UserExceptions(message)
    class PasswordLessThan6CharsException(message: String = "Password cannot be less than 6 chars length") :
        UserExceptions(message)

    class UserNameLessThan3CharsException(message: String = "UserName cannot be less than 3 chars length") :
        UserExceptions(message)

    class InvalidPasswordError : UserExceptions("please Enter a valid password")
    class UserPasswordLessThan8CharsException : UserExceptions("Password must be at least 8 characters")
    class UserPasswordNotContainsNumberException : UserExceptions("Password must contain at least one number")
    class UserPasswordNotContainsUpperCaseException :
        UserExceptions("Password must contain at least one upper case letter")

    class UserPasswordNotContainsLowerCaseException :
        UserExceptions("Password must contain at least one lower case letter")
}