package data.repository

import data.source.user.ExternalUserDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val externalUserDataSource: ExternalUserDataSource,
) : UserRepository {
    override fun deleteUser(userId: String) = executeUserOperation {
        externalUserDataSource.deleteUser(userId)
    }

    override fun createNewUser(user: User) = executeUserOperation {
        externalUserDataSource.createNewUser(user)
    }


    override fun getUserById(userId: String): User =
        executeUserOperation {
            externalUserDataSource.getUserById(userId)
        }


    override fun getAllUsers(): List<User> = executeUserOperation {
        externalUserDataSource.getAllUsers()
    }

    override fun getUserByName(userName: String): User =
        executeUserOperation {
            externalUserDataSource.getUserByName(userName)
        }


    private fun <T> executeUserOperation(operation: () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

