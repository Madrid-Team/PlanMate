package domain.utils

open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class UserNameOrPasswordError : UserExceptions("Wrong password or user name")
    class UserReadWrightException(message: String = "User read write error") : UserExceptions(message)
    class WrongPasswordOrUserName(message: String) : UserExceptions(message)
    class UserExist(message: String = "User already Exist") : UserExceptions(message)
    class UserNotAdminException : UserExceptions("User is not admin")
    class UserNotFoundException(message: String = "User not Found") : UserExceptions(message)
}
