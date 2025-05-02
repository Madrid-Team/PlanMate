package domain.usecases


import data.dto.authentication.UserDto
import domain.repository.UserRepository
import domain.utlis.UserException


class LoginUserUseCase(private val userRepository: UserRepository) {

    fun invoke(userName: String?, passwordHash: String?): UserDto? {
        return if (userName.isNullOrEmpty() || passwordHash.isNullOrEmpty()) {
            throw UserException.NotFoundUser("Not found user or wrong username")

        } else {
            validateUser(userRepository.getAllUsers().getOrNull(), userName, passwordHash)
        }
    }

    private fun validateUser(users: List<UserDto>?, username: String, passwordHash: String): UserDto {
        if (users == null || users.isEmpty()) {
            throw UserException.NotFoundUser("Not found user or wrong username")
        }
        val user = users.find { it.username == username }
            ?: throw UserException.NotFoundUser("Not found user or wrong username")
        if (user.passwordHash != passwordHash) {
            throw UserException.WrongPasswordOrUserName("Wrong  password")
        }
        return user
    }
}