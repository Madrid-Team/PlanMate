package domain.usecases

import domain.models.authentication.User

class CreateUserUseCase {

    operator fun invoke(user: User?): Boolean {
        return false
    }
}