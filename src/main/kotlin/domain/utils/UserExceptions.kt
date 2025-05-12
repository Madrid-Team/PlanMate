package domain.utils

open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class UserNameOrPasswordError : UserExceptions("Wrong password or user name")
    class UserReadWriteException(message: String = "User read write error") : UserExceptions(message)
    class WrongPasswordOrUserName(message: String) : UserExceptions(message)
    class UserExist(message: String = "User already Exist") : UserExceptions(message)
    class UserNotAdminException : UserExceptions("User is not admin")
    class UserNotFoundException(message: String = "User not Found") : UserExceptions(message)
    class UserPasswordLessThan8CharsException : UserExceptions("Password must be at least 8 characters")
    class UserPasswordNotContainsNumberException : UserExceptions("Password must contain at least one number")
    class UserPasswordNotContainsUpperCaseException : UserExceptions("Password must contain at least one upper case letter")
    class UserPasswordNotContainsLowerCaseException : UserExceptions("Password must contain at least one lower case letter")
}
