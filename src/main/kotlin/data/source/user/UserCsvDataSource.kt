package data.source.user

import data.dto.authentication.UserDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
): UserDataSource {
    override fun createUser(user: UserDto): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): Result<UserDto?> {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): Result<List<UserDto>> {
        TODO("Not yet implemented")
    }

    override fun getUserByName(userName: String): Result<UserDto?> {
        TODO("Not yet implemented")
    }
}