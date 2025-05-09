package domain.usecases.user


import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userName: String?, passwordHash: String?): User {
        return if (userName.isNullOrEmpty() || passwordHash.isNullOrEmpty()) {
            throw UserExceptions.UserNameOrPasswordError()
        } else {
            ValidateUser(userRepository).validateUserToLogin(userRepository.getAllUsers(), userName, passwordHash)
        }
    }
}