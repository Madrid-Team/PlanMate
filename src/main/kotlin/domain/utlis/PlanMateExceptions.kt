package domain.utlis

open class PlanMateExceptions(message:String):Exception(message) {
}

class ProjectNameExistException: PlanMateExceptions("Project name already exists")