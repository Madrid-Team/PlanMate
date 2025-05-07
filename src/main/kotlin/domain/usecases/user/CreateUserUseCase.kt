package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun createUser(user: User) {
        userRepository.getUserByName(user.username)?.let {
            throw UserExceptions.UserExist("User already exists")
        }
        val userId = ValidateUser(userRepository).generateUUIDValidToNewUser()
        val newUser = user.copy(id = userId)
        userRepository.createNewUser(newUser)
    }
}