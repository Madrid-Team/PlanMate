package domain.usecases.user


import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.NameValidationResult
import domain.utils.PasswordValidationResult
import domain.utils.UserExceptions

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase

) {
    suspend fun login(userName: String, password: String): User {
        val nameValidation = validateNameUseCase.validateName(userName)
        if (nameValidation is NameValidationResult.NotValid) {
            throw UserExceptions.InvalidUserName()
        }
        val passwordValidation = validatePasswordUseCase.validatePassword(password)
        if (passwordValidation is PasswordValidationResult.NotValid) {
            throw UserExceptions.InvalidPasswordError()
        }
        val passwordHash = PasswordHasher.hash(password)
        val user = userRepository.login(userName, passwordHash)
        return user

    }
}