package domain.usecases.user

import domain.utils.EmptyPasswordException
import domain.utils.PasswordLessThan6CharsException

class ValidatePasswordUseCase {


    fun validatePassword(password: String) {
        if (password.isEmpty()) {
            throw EmptyPasswordException()
        }
        if (password.length < 6) {
            throw PasswordLessThan6CharsException()
        }

    }

}





