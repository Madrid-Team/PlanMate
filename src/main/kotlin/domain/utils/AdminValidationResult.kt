package domain.utils

sealed class AdminValidationResult {
    object Valid : AdminValidationResult()
    object NotAdmin : AdminValidationResult()
}
