package domain.utlis

sealed class UserException(message: String) : RuntimeException(message) {
    class NotFoundUser(message: String) : UserException(message)
    class WrongPasswordOrUserName(message: String) : UserException(message)
    class PermissionDenied(message: String) : UserException(message)
    class InvalidUserNameFormat(message: String) : UserException(message)


}