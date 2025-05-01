package domain.usecases

import domain.repository.UserRepository

class DeleteUser(
    private val userRepository: UserRepository
) {
    fun invoke(userRequestId: String, userToDeleteId: String): Boolean {
        return false
    }
}