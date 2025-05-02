package domain.usecases

import data.dto.authentication.UserDto
import domain.repository.UserRepository
import domain.utlis.UserException
import java.util.*

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun createUser(user: UserDto): Result<Unit> {
        val userId = checkUserIdExist(user.id)
        val userFound = userRepository.getUserByName(user.username).getOrNull()
        return if (userFound != null) {
            Result.failure(UserException.UserExist("Can't Create User already Exist"))
        } else {
            val validUser = UserDto(userId, user.username, user.passwordHash, user.role)
            userRepository.addUser(validUser)
            Result.success(Unit)
        }
    }

    private fun checkUserIdExist(userId: String): String {
        if (userRepository.getUser(userId).getOrNull() == null) {
            val newId = UUID.randomUUID().toString()
            checkUserIdExist(newId)
        }
        return userId
    }
}