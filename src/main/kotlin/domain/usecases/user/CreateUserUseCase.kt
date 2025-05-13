package domain.usecases.user

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.NameValidationResult
import domain.utils.PasswordValidationResult
import domain.utils.UserExceptions
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val passwordHashUseCase: PasswordHashUseCase,
) {
    suspend fun createUser(userName: String, password: String) {
        val nameValidation = validateNameUseCase.validateName(userName)
        if (nameValidation is NameValidationResult.NotValid) {
            throw UserExceptions.InvalidUserName()
        }
        val passwordValidation = validatePasswordUseCase.validatePassword(password)
        if (passwordValidation is PasswordValidationResult.NotValid) {
            throw UserExceptions.InvalidPasswordError()
        }
        val passwordHash = passwordHashUseCase.passwordHash(password)
        val user = User(
            id = UUID.randomUUID(),
            username = userName,
            passwordHash = passwordHash,
            role = UserRole.MATE.name,
        )
        userRepository.createNewUser(user)
    }
}