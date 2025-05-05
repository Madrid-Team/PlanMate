package domain.utlis

open class ProjectExceptions(message: String) : PlanMateExceptions(message) {
    class ProjectNotFoundException : ProjectExceptions("Project not found")
   // class PasswordIsTooWeakException : ProjectExceptions("Password is too weak")
  //  class ProjectNameExistException : ProjectExceptions("Project name already exists")
    class ProjectNameInvalidException : ProjectExceptions("Project name invalid,please enter a valid name")
   // class ProjectDescriptionInvalidException : ProjectExceptions("Project description invalid,please enter a valid description")
 //   class ProjectStatesInvalidException : ProjectExceptions("Project states shouldn't be empty ")
  //  class ProjectTaskStatesInvalidException : ProjectExceptions("Task states shouldn't be empty ")
    class ProjectsFileNotExistsException : ProjectExceptions("projects.csv not found")
    class ProjectsReadWriteException : ProjectExceptions("Couldn't access projects.csv")

}