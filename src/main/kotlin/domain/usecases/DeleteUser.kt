package domain.usecases

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utlis.UserException

class DeleteUser(
    private val userRepository: UserRepository
) {
    fun invoke(userRequestId: String, userToDeleteId: String): Boolean {

        idsIsValid(userRequestId, userToDeleteId)

        val requiredUser = getUserOrThrowException(userRequestId)
        requiredUserIsValid(requiredUser)

        val userToDelete = getUserOrThrowException(userToDeleteId)
        userToDeleteIsValid(userToDelete)

        userRepository.deleteUser(userToDeleteId)
        return true
    }


    private fun idsIsValid(userRequestId: String, userToDeleteId: String) {
        if (userRequestId.isBlank() || userToDeleteId.isBlank()) {
            throw UserException.InvalidInput("User id must be non-blank")
        }
    }

    private fun getUserOrThrowException(userId: String): User {
        return userRepository.getUser(userId)
            ?: throw UserException.NotFoundUser("User not found")
    }

    private fun requiredUserIsValid(user: User) {
        if (user.role != UserRole.ADMIN)
            throw UserException.PermissionDenied("This user Not allowed to delete another user")
    }

    private fun userToDeleteIsValid(user: User) {
        if (user.role == UserRole.ADMIN)
            throw UserException.PermissionDenied("Admin can't delete another admin")
    }

}