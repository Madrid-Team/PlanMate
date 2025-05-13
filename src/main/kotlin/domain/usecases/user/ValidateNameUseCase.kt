package domain.usecases.user

import domain.utils.UserExceptions

class ValidateNameUseCase {

    fun validateName(name: String) {
        if (name.isBlank()) {
            throw UserExceptions.InvalidUserName()
        }
        if (name.length < 3) {
             throw UserExceptions.UserNameLessThan3CharsException()
        }

    }

}
