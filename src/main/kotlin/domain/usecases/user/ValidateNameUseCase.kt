package domain.usecases.user

import domain.utils.NameValidationResult
import domain.utils.UserExceptions

class ValidateNameUseCase {

    fun validateName(name: String): NameValidationResult {
        if (name.isBlank()) {
            return NameValidationResult.NotValid(UserExceptions.EmptyUserNameException().message.toString())
        }
        if (name.length < 3) {
            return NameValidationResult.NotValid(UserExceptions.UserNameLessThan3CharsException().message.toString())
        }


        return NameValidationResult.Valid
    }


}
