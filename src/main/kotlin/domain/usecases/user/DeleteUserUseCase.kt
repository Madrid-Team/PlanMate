package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utlis.UserExceptions

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    fun invoke(userRequestId: String, userToDeleteId: String) {
        val user = userRepository.getUserById(userRequestId)
            ?: throw UserExceptions.UserNotFoundException()

        userRepository.getUserById(userToDeleteId)
            ?: throw UserExceptions.UserNotFoundException()

        if (user.role == UserRole.ADMIN.name) {
            userRepository.deleteUser(userToDeleteId)
        } else {
            throw UserExceptions.UserNotAdminException()
        }
    }
}