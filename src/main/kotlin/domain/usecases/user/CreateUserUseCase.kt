package domain.usecases.user

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun createUser(userName: String, password: String) {
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