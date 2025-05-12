package domain.usecases.user

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.PasswordValidation
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordValidation: PasswordValidation
) {
    suspend fun createUser(userName: String, password: String) {
        passwordValidation.validate(password)
        val passwordHash = PasswordHasher.hash(password)
        val newUser = User(
            username = userName,
            passwordHash = passwordHash,
            role = UserRole.MATE.name,
            id = UUID.randomUUID()
        )
        userRepository.createNewUser(newUser)
    }
}