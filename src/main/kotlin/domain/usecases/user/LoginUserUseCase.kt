package domain.usecases.user


import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.UserExceptions
import domain.usecases.user.ValidateUser

class LoginUserUseCase(private val userRepository: UserRepository) {
    val validator = ValidateUser(userRepository)
    suspend  fun login(userName: String, passwordHash: String): User {
       val users = userRepository.getAllUsers()
       return validator.validateUser(users,userName,passwordHash)

    }

}