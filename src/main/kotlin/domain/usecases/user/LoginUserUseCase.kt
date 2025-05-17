package domain.usecases.user


import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.repository.UserRepository

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