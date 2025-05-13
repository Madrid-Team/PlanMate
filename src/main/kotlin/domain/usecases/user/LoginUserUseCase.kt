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
        validateNameUseCase.validateName(userName)
        validatePasswordUseCase.validatePassword(password)

        val passwordHash = PasswordHasher.hash(password)
        val user = userRepository.login(userName, passwordHash)
        return user

    }
}