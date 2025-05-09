package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.validation.ValidateUser

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        val userId = ValidateUser(userRepository).generateUUIDValidToNewUser()
        val newUser = user.copy(id = userId)
        userRepository.createNewUser(newUser)
    }
}