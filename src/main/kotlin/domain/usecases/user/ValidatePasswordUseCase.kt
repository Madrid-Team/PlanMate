package domain.usecases.user

import domain.utils.UserExceptions

class ValidatePasswordUseCase {


    fun validatePassword(password: String) {
        if (password.isEmpty()) {
            throw UserExceptions.EmptyPasswordException()
        }
        if (password.length < 6) {
            throw UserExceptions.PasswordLessThan6CharsException()
        }

    }

}





