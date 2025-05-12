package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.UserExceptions
import domain.usecases.user.ValidateUser

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun deleteUser(userRequestId: String, userToDeleteId: String) {
        val user = userRepository.getUserById(userRequestId)
        userRepository.getUserById(userToDeleteId)
        ValidateUser(userRepository).validateUserRoleToDelete(user.role,userToDeleteId)
    }
}