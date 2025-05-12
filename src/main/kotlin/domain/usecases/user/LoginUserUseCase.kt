package domain.usecases.user


import domain.models.authentication.User
import domain.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userName: String?, passwordHash: String?): User {
        return userRepository.login(userName!!,passwordHash!!)
    }
}