package domain.usecases.user

import data.utils.PasswordHasher

class PasswordHashUseCase {
    fun passwordHash(password : String) : String{
        return PasswordHasher.hash(password)
    }

}