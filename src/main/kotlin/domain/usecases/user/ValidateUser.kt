package domain.usecases.user

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.UserExceptions
import java.util.UUID

class ValidateUser(
    private val userRepository: UserRepository
) {
    fun validateUser(users: List<User>?, username: String, passwordHash: String): User {
        if (users == null || users.isEmpty()) {
            throw UserExceptions.UserNotFoundException("Not found user or wrong username")
        }
        if (username.isEmpty() || passwordHash.isEmpty()) {
            throw UserExceptions.UserNameOrPasswordError()
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


    suspend fun validateUserRoleToDelete(role : String, userToDeleteId : String){
        if (role == UserRole.ADMIN.name) {
            userRepository.deleteUser(userToDeleteId)
        } else {
            throw UserExceptions.UserNotAdminException()
        }


    }


}