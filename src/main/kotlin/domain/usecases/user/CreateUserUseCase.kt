package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.usecases.user.ValidateUser

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend  fun createUser(user: User) {
        val userId = ValidateUser(userRepository).generateUUIDValidToNewUser()
        val newUser = user.copy(id = userId)
        userRepository.createNewUser(newUser)
    }
}