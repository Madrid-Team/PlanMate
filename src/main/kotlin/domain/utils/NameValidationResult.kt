package domain.utils

sealed class NameValidationResult {
    object Valid : NameValidationResult()
    class NotValid(val message: String) : NameValidationResult()
}