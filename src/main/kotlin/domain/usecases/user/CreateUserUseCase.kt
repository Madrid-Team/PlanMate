package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.usecases.user.ValidateUser
import domain.utils.NameValidationResult
import domain.utils.PasswordValidationResult
import domain.utils.UserExceptions

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {
    suspend fun createUser(user: User) {
        val nameValidation = validateNameUseCase.validateName(user.username)
        if (nameValidation is NameValidationResult.NotValid) {
            throw UserExceptions.InvalidUserName()
        }
        val passwordValidation = validatePasswordUseCase.validatePassword(user.passwordHash)
        if (passwordValidation is PasswordValidationResult.NotValid) {
            throw UserExceptions.InvalidPasswordError()
        }
        userRepository.createNewUser(user)
}
    }