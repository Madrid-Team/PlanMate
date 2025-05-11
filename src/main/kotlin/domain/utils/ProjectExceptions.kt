package domain.utils

open class ProjectExceptions(message: String) : PlanMateExceptions(message){
    class ProjectNotFoundException : ProjectExceptions("Project not found")
    class ProjectNameInvalidException : ProjectExceptions("Project name invalid,please enter a valid name")
    class ProjectsFileNotExistsException : ProjectExceptions("projects.csv not found")
    class ProjectsReadWriteException : ProjectExceptions("Couldn't access projects.csv")

}