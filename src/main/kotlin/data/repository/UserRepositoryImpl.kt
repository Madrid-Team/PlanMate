package data.repository

import data.mapper.toDto
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import domain.models.authentication.User
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userCsvParser: UserCsvParser,
) : UserRepository {
    override fun deleteUser(userId: String): Result<Unit> {
        return userDataSource.deleteUser(userId)
    }

    override fun createNewUser(user: User): Result<Unit> {
        val row: String = userCsvParser.parseUserToRow(user.toDto())
        return userDataSource.createNewUser(row)
    }

    override fun getUserById(userId: String): Result<User?> {
        return userDataSource.getUserById(userId)
    }

    override fun getAllUsers(): Result<List<User>> {
        return userDataSource.getAllUsers()
    }

    override fun getUserByName(userName: String): Result<User?> {
        return userDataSource.getUserByName(userName)
    }
}