package domain.utlis

sealed class UserException(message: String) : RuntimeException(message) {
    class NotFoundUser(message: String) : UserException(message)
    class InvalidInput(message: String) : UserException(message)
    class PermissionDenied(message: String) : UserException(message)
    class UserExist(message: String = "User already Exist") : UserException(message)
    class WrongPasswordOrUserName(message: String = "User Not Exist") : UserException(message)
    class InvalidUserNameFormat(message: String = "User Not Exist") : UserException(message)
    data object UserIsNullException : UserException("User is null")
    data object UserAlreadyExistsException : UserException("User already exists")
    data object UserDoesNotHavePermissionException : UserException("User does not have permission")
    data object UserNotFoundException : UserException("User not found")
}