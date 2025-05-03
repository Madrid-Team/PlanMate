package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utlis.UserExceptions

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    fun invoke(userRequestId: String, userToDeleteId: String): Result<Unit> {
        return try {
            val user = userRepository.getUserById(userRequestId)
            val userResponse = user.fold(
                onSuccess = { it },
                onFailure = { return Result.failure(it) }
            )
           userRepository.getUserById(userToDeleteId).fold(
                onSuccess = {it},
                onFailure = {
                    return Result.failure(UserExceptions.UserNotFoundException())
                }
            )

            if (userResponse?.role == UserRole.ADMIN.name) {
                userRepository.deleteUser(userToDeleteId)
            } else {
                return Result.failure(Exception("User is not an admin"))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}