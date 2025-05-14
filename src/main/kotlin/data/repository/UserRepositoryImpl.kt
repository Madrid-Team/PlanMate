package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.csv.user.CurrentUserProvider
import data.source.csv.user.UserExternalDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.UserExceptions

class UserRepositoryImpl(
    private val userExternalDataSource: UserExternalDataSource,
    private val currentUserProvider: CurrentUserProvider,
) : UserRepository {
    override suspend fun deleteUserByUserId(userId: String) = executeUserOperation {
        userExternalDataSource.deleteUser(userId)
    }

    override suspend fun createNewUser(user: User) = executeUserOperation {

        userExternalDataSource.createNewUser(user.toDto())

    }


    override suspend fun getUserByUserId(userId: String): User =  executeUserOperation {
        val user = userExternalDataSource.getUserById(userId)
        user?.let { currentUserProvider.setCurrentUser(it) }
        user?.toDomain() ?: throw UserExceptions.UserNotFoundException()
    }



    override suspend fun getAllUsers(): List<User> =  executeUserOperation {
        val users = userExternalDataSource.getAllUsers().map { it.toDomain() }
        if (users.isEmpty()) throw UserExceptions.UserNotFoundException()
        users
    }


    override suspend fun getUserByName(userName: String): User  = executeUserOperation {
        val user = userExternalDataSource.getUserByName(userName)
        user?.let { currentUserProvider.setCurrentUser(it) }
        user?.toDomain() ?: throw UserExceptions.UserNotFoundException()
    }



    override suspend fun login(username: String, password: String): User = executeUserOperation {
        val user = userExternalDataSource.login(username, password)
        user?.let { currentUserProvider.setCurrentUser(it) }
        user?.toDomain() ?: throw UserExceptions.UserNotFoundException()
    }



    private suspend fun <T> executeUserOperation(operation: suspend () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

