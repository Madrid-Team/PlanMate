package domain.usecases.user

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val passwordHashUseCase: PasswordHashUseCase,
) {
    suspend fun createUser(userName: String, password: String) {
        validateNameUseCase.validateName(userName)
        validatePasswordUseCase.validatePassword(password)

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