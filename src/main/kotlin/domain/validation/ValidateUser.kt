package domain.validation

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserException
import java.util.UUID

class ValidateUser(
    private val userRepository: UserRepository
) {
    fun validateUserToLogin(users: List<User>?, username: String, passwordHash: String): User {
        if (users == null || users.isEmpty()) {
            throw UserException.NotFoundUser("Not found user or wrong username")
        }
        val user = users.find { it.username == username }
            ?: throw UserException.NotFoundUser("Not found user or wrong username")
        if (user.passwordHash != passwordHash) {
            throw UserException.WrongPasswordOrUserName("Wrong  password")
        }
        return user
    }

    fun generateUUIDValidToNewUser(): UUID {
        val newId = UUID.randomUUID()
        return when (userRepository.getUserById(newId.toString()).getOrNull()) {
            null -> newId
            else -> generateUUIDValidToNewUser()
        }
    }
}