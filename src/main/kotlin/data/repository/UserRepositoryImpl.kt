package data.repository

import data.source.user.UserDataSource
import domain.models.authentication.User
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository  {
    override fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): User? {
        TODO("Not yet implemented")
    }
}