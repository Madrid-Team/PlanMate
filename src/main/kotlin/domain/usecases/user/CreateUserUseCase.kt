package domain.usecases.user

import data.dto.authentication.UserDto
import domain.repository.UserRepository
import domain.utlis.UserException
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun createUser(user: UserDto): Result<Unit> {
        return try {

            val checkingUsername = userRepository.getUserByName(user.username).getOrNull()
            if (checkingUsername != null) {
                return Result.failure(UserException.UserExist("Username '${user.username}' already exists"))
            }

            val userId = checkUserIdExist()
            val newUser = user.copy(id = userId)
            userRepository.addUser(newUser).fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun checkUserIdExist(): String {
        val newId = UUID.randomUUID().toString()
        return when (userRepository.getUser(newId).getOrNull()) {
            null -> newId
            else -> checkUserIdExist()
        }
    }
}