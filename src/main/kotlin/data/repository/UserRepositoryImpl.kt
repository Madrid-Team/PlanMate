package data.repository

import data.source.user.UserDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override fun deleteUser(userId: String) = executeUserOperation {
        userDataSource.deleteUser(userId)
    }

    override fun createNewUser(user: User) = executeUserOperation {
        userDataSource.createNewUser(user)
    }


    override fun getUserById(userId: String): User =
        executeUserOperation {
            userDataSource.getUserById(userId)
        }


    override fun getAllUsers(): List<User> = executeUserOperation {
        userDataSource.getAllUsers()
    }

    override fun getUserByName(userName: String): User =
        executeUserOperation {
            userDataSource.getUserByName(userName)
        }


    private fun <T> executeUserOperation(operation: () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

