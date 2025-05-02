package domain.usecases

import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import domain.repository.UserRepository
import domain.utlis.UserException

class DeleteUser(
    private val userRepository: UserRepository
) {
    fun invoke(userRequestId: String, userToDeleteId: String): Boolean {

        idsIsValid(userRequestId, userToDeleteId)

        val requiredUser = getUserOrThrowException(userRequestId).getOrThrow()
        requiredUserIsValid(requiredUser)

        val userToDelete = getUserOrThrowException(userToDeleteId).getOrThrow()
        userToDeleteIsValid(userToDelete)

        return userRepository.deleteUser(userToDeleteId)
    }


    private fun idsIsValid(userRequestId: String, userToDeleteId: String) {
        if (userRequestId.isBlank() || userToDeleteId.isBlank()) {
            throw UserException.InvalidInput("User id must be non-blank")
        }
    }

    private fun getUserOrThrowException(userId: String): Result<UserDto> {
        return userRepository.getUser(userId).map {
            it ?: throw UserException.NotFoundUser("User with id $userId not found")
        }
    }

    private fun requiredUserIsValid(user: UserDto) {
        if (user.role != UserRoleDto.ADMIN)
            throw UserException.PermissionDenied("This user Not allowed to delete another user")
    }

    private fun userToDeleteIsValid(user: UserDto) {
        if (user.role == UserRoleDto.ADMIN)
            throw UserException.PermissionDenied("Admin can't delete another admin")
    }

}