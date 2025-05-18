package domain.utils

class PasswordValidation {

    fun validate(password: String) {
        when {
            password.length < 8 -> throw UserPasswordLessThan8CharsException()
            !password.contains("[a-z]".toRegex()) -> throw UserPasswordNotContainsLowerCaseException()
            !password.contains("[A-Z]".toRegex()) -> throw UserPasswordNotContainsUpperCaseException()
            !password.contains("[0-9]".toRegex()) -> throw UserPasswordNotContainsNumberException()
        }
    }
}