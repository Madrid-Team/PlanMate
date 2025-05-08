package domain.utlis

open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class UserNameOrPasswordError : UserExceptions("Wrong password or user name")
    class UserReadWrightException(message: String = "User read write error") : UserExceptions(message)

    //    class InvalidInput(message: String) : UserExceptions(message)
    class WrongPasswordOrUserName(message: String) : UserExceptions(message)

    //    class PermissionDenied(message: String) : UserExceptions(message)
    class UserExist(message: String = "User already Exist") : UserExceptions(message)

    //    class InvalidUserNameFormat(message: String = "User Not Exist") : UserExceptions(message)
//    class UserIsNullException : UserExceptions("User is null")
//    class UserAlreadyExistsException : UserExceptions("User already exists")
//    class UserDoesNotHavePermissionException : UserExceptions("User does not have permission")
    class UserNotAdminException : UserExceptions("User is not admin")
    class UserNotFoundException(message: String = "User not Found") : UserExceptions(message)
}
