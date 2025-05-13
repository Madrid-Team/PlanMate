package domain.utils

open class ProjectExceptions(message: String) : PlanMateExceptions(message) {
    class ProjectNotFoundException(message: String = "Project not found") : ProjectExceptions(message)
    class ProjectNameInvalidException : ProjectExceptions("Project name invalid,please enter a valid name")
    class ProjectStatesIsEmptyException(error: String = "Project States is empty") : ProjectExceptions(error)
    class ProjectDescriptionIsEmptyException(error: String = "Project description is empty") : ProjectExceptions(error)
    class ProjectTaskStatesIsEmptyException(error: String = "Project task states is empty") : ProjectExceptions(error)
    class ProjectsFileNotExistsException : ProjectExceptions("projects.csv not found")
    class ProjectsReadWriteException : ProjectExceptions("Couldn't access projects.csv")
    class NoChangesException(message: String) : ProjectExceptions(message)
    class NoLogsFoundException(error: String = "There are no logs found for this task") : ProjectExceptions(error)
    class ProjectDescriptionInvalidException :
        ProjectExceptions("Project description invalid, please enter a valid description")
    class ProjectDescriptionTooShortException : ProjectExceptions("Project description is too short")
}