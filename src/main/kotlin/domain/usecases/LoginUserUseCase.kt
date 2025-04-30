package domain.usecases

import domain.repository.UserRepository


class LoginUserUseCase ( private val userRepository: UserRepository){
    fun invoke(userName: String, password: String): Boolean {
        return false
    }
}