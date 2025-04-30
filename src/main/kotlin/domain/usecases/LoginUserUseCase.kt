package domain.usecases


import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserException


class LoginUserUseCase ( private val userRepository: UserRepository){

    fun invoke(userName: String,password: String): User{

        return validateUser(userRepository.getALLUser().values.toList(), userName, password)
    }
    private fun validateUser(users: List<User>, username: String, password: String): User{
        val user=users.find { it.username==username }
            ?: throw UserException.NotFoundUser("Not found user or wrong username")
        if(user.passwordHash!=password){
            throw UserException.WrongPasswordOrUserName("Wrong  password")
        }
        return user
    }

}