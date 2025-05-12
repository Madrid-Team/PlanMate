package domain.utils

sealed class PasswordValidationResult {
    object Valid : PasswordValidationResult()
    class NotValid(message : String) : PasswordValidationResult()

}