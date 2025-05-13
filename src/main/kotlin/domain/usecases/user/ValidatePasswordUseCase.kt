package domain.usecases.user

import domain.utils.PasswordValidationResult
import domain.utils.UserExceptions

class ValidatePasswordUseCase {


    fun validatePassword(password: String): PasswordValidationResult {
        if (password.isEmpty()) {
            return PasswordValidationResult.NotValid(UserExceptions.EmptyPasswordException().message.toString())
        }
        if (password.length < 6) {
            return PasswordValidationResult.NotValid(UserExceptions.PasswordLessThan6CharsException().message.toString())
        }
//        if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,}\$".toRegex())){
//            return PasswordValidationResult.NotValid(UserExceptions.InvalidPasswordError().message.toString())
//        }

        return PasswordValidationResult.Valid
    }

}





