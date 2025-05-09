package domain.validation

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import java.util.*

class ValidateUser(
    private val userRepository: UserRepository
) {
    fun validateUserToLogin(users: List<User>?, username: String, passwordHash: String): User {
        if (users == null || users.isEmpty()) {
            throw UserExceptions.UserNotFoundException("Not found user or wrong username")
        }
        val user = users.find { it.username == username }
            ?: throw UserExceptions.UserNotFoundException("Not found user or wrong username")
        if (user.passwordHash != passwordHash) {
            throw UserExceptions.WrongPasswordOrUserName("Wrong  password")
        }
        return user
    }

    suspend fun generateUUIDValidToNewUser(): UUID {
        val newId = UUID.randomUUID()

        return try {
            userRepository.getUserById(newId.toString())
            generateUUIDValidToNewUser()
        } catch (_: UserExceptions.UserNotFoundException) {
            newId
        }catch (e: Exception) {
            throw e
        }
    }
}