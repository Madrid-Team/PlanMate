package data.repository

import data.mapper.toDto
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userCsvParser: UserCsvParser,
) : UserRepository {
    override fun deleteUser(userId: String) = executeUserOperation {
        userDataSource.deleteUser(userId)
    }

    override fun createNewUser(user: User) = executeUserOperation {
        val row: String = userCsvParser.parseUserToRow(user.toDto())
        userDataSource.createNewUser(row)
    }


    override fun getUserById(userId: String): User? =
        executeUserOperation {
            userDataSource.getUserById(userId)
        }


    override fun getAllUsers(): List<User> = executeUserOperation {
        userDataSource.getAllUsers()
    }

    override fun getUserByName(userName: String): User? =
        executeUserOperation {
            userDataSource.getUserByName(userName) ?: throw UserExceptions.UserNotFoundException()
        }


    private fun <T> executeUserOperation(operation: () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

