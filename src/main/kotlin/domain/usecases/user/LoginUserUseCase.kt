package domain.usecases.user


import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser

class LoginUserUseCase(private val userRepository: UserRepository) {

    fun invoke(userName: String?, passwordHash: String?): User? {
        return if (userName.isNullOrEmpty() || passwordHash.isNullOrEmpty()) {
            throw UserExceptions.NotFoundUser("Not found user or wrong username")
        } else {
            ValidateUser(userRepository).validateUserToLogin(userRepository.getAllUsers().getOrNull(), userName, passwordHash)
        }
    }
}