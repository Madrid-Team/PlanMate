package data.source.user

import data.dto.authentication.User
import data.utils.FileCsvReader
import data.utils.FileCsvWriter

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
): UserDataSource {
    override fun createUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): Result<User?> {
        TODO("Not yet implemented")
    }
}