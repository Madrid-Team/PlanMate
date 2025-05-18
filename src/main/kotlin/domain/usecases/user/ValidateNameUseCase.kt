package domain.usecases.user

import domain.utils.InvalidUserName
import domain.utils.UserExceptions
import domain.utils.UserNameLessThan3CharsException

class ValidateNameUseCase {

    fun validateName(name: String) {
        if (name.isBlank()) {
            throw InvalidUserName()
        }
        if (name.length < 3) {
             throw UserNameLessThan3CharsException()
        }

    }

}
