package domain.usecases

import data.dto.authentication.UserDto
import domain.repository.UserRepository
import domain.utlis.UserException

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun createUser(user: UserDto): Result<Unit>{
        val userFound= userRepository.getUserByName(user.username).getOrNull()
        return if (userFound!= null) {
            Result.failure(UserException.UserExist("Can't Create User already Exist"))
        } else {
            userRepository.addUser(user)
            Result.success(Unit)
        }
    }
}