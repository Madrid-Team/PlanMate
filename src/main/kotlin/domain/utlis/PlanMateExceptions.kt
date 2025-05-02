package domain.utlis

open class PlanMateExceptions(message: String) : Exception(message) {
}

class ProjectNotFoundException : PlanMateExceptions("Project not found")
data object PasswordIsTooWeakException : PlanMateExceptions("Password is too weak")

class ProjectNameExistException: PlanMateExceptions("Project name already exists")
class ProjectNameInvalidException: PlanMateExceptions("Project name invalid,please enter a valid name")
class ProjectDescriptionInvalidException: PlanMateExceptions("Project description invalid,please enter a valid description")
class ProjectStatesInvalidException: PlanMateExceptions("Project states shouldn't be empty ")
class ProjectTaskStatesInvalidException: PlanMateExceptions("Task states shouldn't be empty ")
class ProjectsFileNotExistsException: PlanMateExceptions("projects.csv not found")
class ProjectsReadWriteException: PlanMateExceptions("Couldn't access projects.csv")