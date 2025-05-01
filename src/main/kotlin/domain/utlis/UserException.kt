package domain.utlis

sealed class UserException(message: String) : RuntimeException(message) {
    class NotFoundUser(message: String) : UserException(message)
    class InvalidInput(message: String) : UserException(message)
    class PermissionDenied(message: String) : UserException(message)
    class UserExist(message: String="User already Exist") : UserException(message)
}