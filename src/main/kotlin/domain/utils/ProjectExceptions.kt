package domain.utils

open class ProjectExceptions(message: String) : PlanMateExceptions(message){
    class ProjectNotFoundException(message: String = "Project not found") : ProjectExceptions(message)
    class ProjectNameInvalidException : ProjectExceptions("Project name invalid,please enter a valid name")
    class ProjectsFileNotExistsException : ProjectExceptions("projects.csv not found")
    class ProjectsReadWriteException : ProjectExceptions("Couldn't access projects.csv")
    class NoChangesException(message: String) : ProjectExceptions(message)
}