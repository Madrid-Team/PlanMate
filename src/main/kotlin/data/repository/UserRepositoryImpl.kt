package data.repository

import data.dto.authentication.UserDto
import data.source.user.UserCsvDataSource
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userCsvDataSource: UserCsvDataSource,
    private val userCsvParser: UserCsvParser,
): UserRepository  {
    override fun deleteUser(userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun addUser(user: UserDto): Result<Unit> {
        val row: String =userCsvParser.parseUserToRow(user)
        return userDataSource.createUser(row)

    }

    override fun getUser(userId: String): Result<UserDto?> {
      return userCsvDataSource.getUser(userId)

    }

    override fun getAllUsers(): Result<List<UserDto>> {
        return userDataSource.getAllUsers()
    }

    override fun getUserByName(userName: String):  Result<UserDto?>{
        return userDataSource.getUserByName(userName)
    }
}