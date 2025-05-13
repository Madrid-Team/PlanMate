package domain.utils

class PasswordValidation {

    fun validate(password: String) {
        when {
            password.length < 8 -> throw UserExceptions.UserPasswordLessThan8CharsException()
            !password.contains("[a-z]".toRegex()) -> throw UserExceptions.UserPasswordNotContainsLowerCaseException()
            !password.contains("[A-Z]".toRegex()) -> throw UserExceptions.UserPasswordNotContainsUpperCaseException()
            !password.contains("[0-9]".toRegex()) -> throw UserExceptions.UserPasswordNotContainsNumberException()
        }
    }
}