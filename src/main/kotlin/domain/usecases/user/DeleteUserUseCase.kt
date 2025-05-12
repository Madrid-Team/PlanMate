package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.UserExceptions

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun deleteUser(userRequestId: String, userToDeleteId: String) {
        val user = userRepository.getUserById(userRequestId)
        userRepository.getUserById(userToDeleteId)
        if (user.role == UserRole.ADMIN.name) {
            userRepository.deleteUser(userToDeleteId)
        } else {
            throw UserExceptions.UserNotAdminException()
        }
    }
}