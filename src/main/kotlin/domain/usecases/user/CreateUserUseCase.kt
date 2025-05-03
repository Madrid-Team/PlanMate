package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserException
import domain.validation.ValidateUser

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun createUser(user: User): Result<Unit> {
        userRepository.getUserByName(user.username).getOrNull()?.let {
            return Result.failure(UserException.UserExist("User already exists"))
        }
        val userId = ValidateUser(userRepository).generateUUIDValidToNewUser()
        val newUser = user.copy(id = userId)
        return userRepository.createNewUser(newUser).fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { Result.failure(it) }
        )
    }
}