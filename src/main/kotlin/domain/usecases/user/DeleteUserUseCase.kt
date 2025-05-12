package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.UserExceptions

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    val validator = ValidateUser(userRepository)
    suspend fun deleteUser(userRequestId: String, userToDeleteId: String) {
        val user = userRepository.getUserById(userRequestId)
        userRepository.getUserById(userToDeleteId)
        validator.validateUserRoleToDelete(user.role,userToDeleteId)
    }
}