package data.repository

import data.source.user.ExternalUserDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val externalUserDataSource: ExternalUserDataSource,
) : UserRepository {
    override suspend fun deleteUser(userId: String) = executeUserOperation {
        externalUserDataSource.deleteUser(userId)
    }

    override suspend fun createNewUser(user: User) = executeUserOperation {
        externalUserDataSource.createNewUser(user)
    }


    override suspend fun getUserById(userId: String): User =
        executeUserOperation {
            externalUserDataSource.getUserById(userId)
        }


    override suspend fun getAllUsers(): List<User> = executeUserOperation {
        externalUserDataSource.getAllUsers()
    }

    override suspend fun getUserByName(userName: String): User =
        executeUserOperation {
            externalUserDataSource.getUserByName(userName)
        }


    private suspend fun <T> executeUserOperation(operation: suspend () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

