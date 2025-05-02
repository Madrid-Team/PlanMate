package domain.utlis

open class PlanMateExceptions(message: String) : Exception(message) {
}

class ProjectNameExistException : PlanMateExceptions("Project name already exists")
class ProjectNotFoundException : PlanMateExceptions("Project not found")
data object PasswordIsTooWeakException : PlanMateExceptions("Password is too weak")
