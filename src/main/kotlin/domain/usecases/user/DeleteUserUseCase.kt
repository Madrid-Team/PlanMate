package domain.usecases.user

import domain.repository.UserRepository
import domain.utils.UserExceptions

class DeleteUserUseCase(
    private val userRepository: UserRepository,
    private val validateAdminRoleUseCase: ValidateAdminRoleUseCase
) {
    suspend fun deleteUser(userRequestId: String, userToDeleteId: String) {
        val user = userRepository.getUserByUserId(userRequestId)
        val isAdmin = validateAdminRoleUseCase.validate(user.role)
        if (isAdmin) {
            userRepository.deleteUserByUserId(userToDeleteId)
        } else {
            throw UserExceptions.UserNotAdminException()
        }
    }
}