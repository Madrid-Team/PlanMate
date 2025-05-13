package domain.utils

open class UserExceptions(message: String) : PlanMateExceptions(message) {
    class UserNameOrPasswordError : UserExceptions("Wrong password or user name")
    class UserReadWriteException(message: String = "User read write error") : UserExceptions(message)
    class InvalidUserName() : UserExceptions(message= "Invalid username")
    class UserExist(message: String = "User already Exist") : UserExceptions(message)
    class UserNotAdminException : UserExceptions("User is not admin")
    class UserNotFoundException(message: String = "User not Found") : UserExceptions(message)
    class EmptyPasswordException(message: String = "Password Cannot be empty") : UserExceptions(message)
    class PasswordLessThan6CharsException(message: String = "Password cannot be less than 6 chars length") : UserExceptions(message)
    class EmptyUserNameException(message: String = "UserName Cannot be empty") : UserExceptions(message)
    class UserNameLessThan3CharsException(message: String = "UserName cannot be less than 3 chars length") : UserExceptions(message)
    class InvalidPasswordError : UserExceptions("please Enter a valid password")


    class UserPasswordLessThan8CharsException : UserExceptions("Password must be at least 8 characters")
    class UserPasswordNotContainsNumberException : UserExceptions("Password must contain at least one number")
    class UserPasswordNotContainsUpperCaseException : UserExceptions("Password must contain at least one upper case letter")
    class UserPasswordNotContainsLowerCaseException : UserExceptions("Password must contain at least one lower case letter")
}
