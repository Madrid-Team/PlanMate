package domain.utlis

open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class NotFoundUser(message: String) : UserExceptions(message)
    class InvalidInput(message: String) : UserExceptions(message)
    class WrongPasswordOrUserName(message: String) : UserExceptions(message)
    class PermissionDenied(message: String) : UserExceptions(message)
    class UserExist(message: String = "User already Exist") : UserExceptions(message)
    class InvalidUserNameFormat(message: String = "User Not Exist") : UserExceptions(message)
    class UserIsNullException : UserExceptions("User is null")
    class UserAlreadyExistsException : UserExceptions("User already exists")
    class UserDoesNotHavePermissionException : UserExceptions("User does not have permission")
    class UserNotFoundException : UserExceptions("User not found")
}
