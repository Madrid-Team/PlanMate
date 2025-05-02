package domain.usecases


import data.dto.authentication.UserDto
import data.utils.PasswordHasher
import domain.repository.UserRepository
import domain.utlis.UserException


class LoginUserUseCase ( private val userRepository: UserRepository   ){

    fun invoke(userName: String?,password: String?): UserDto?{
        return if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
            throw  UserException.NotFoundUser("Not found user or wrong username")

        }else {
            validateUser(userRepository.getAllUsers().getOrNull(), userName, password)
        }
        }
    private fun validateUser(users: List<UserDto>?, username: String, password: String): UserDto{
        if (users == null || users.isEmpty()){
            throw UserException.NotFoundUser("Not found user or wrong username")
        }
        val user=users.find { it.username==username }
            ?: throw UserException.NotFoundUser("Not found user or wrong username")
        if(user.passwordHash!= PasswordHasher.hash(password)){
            throw UserException.WrongPasswordOrUserName("Wrong  password")
        }

        return user
    }

}