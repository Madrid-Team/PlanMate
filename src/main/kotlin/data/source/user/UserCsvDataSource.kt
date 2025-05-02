package data.source.user

import data.dto.authentication.UserDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.utlis.UserException

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
) : UserDataSource {
    override fun createUser(userAsRow: String): Result<Unit> {
        try {
            fileCsvWriter.writeToCsvFile(userAsRow)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun deleteUser(userId: String): Result<Unit> {
        val allUsers = getAllUsers().getOrThrow()
        val updatedUsers = allUsers.filter { it.id != userId }

        if (allUsers.size == updatedUsers.size) return Result.failure(UserException.UserNotFoundException)

        val userRows = updatedUsers.map { user ->
            userCsvParser.parseUserToRow(user)
        }


        fileCsvWriter.updateCsvFile(if (userRows.isEmpty()) "" else userRows.joinToString("\n"))
        return Result.success(Unit)
    }

    override fun getUser(userId: String): Result<UserDto?> {
        return try {
            val user = getAllUsers().getOrThrow().firstOrNull { userId == it.id }
            Result.success(user)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong"))
        }
    }

    override fun getAllUsers(): Result<List<UserDto>> {
        return try {
            val rows = fileCsvReader.readCsvFile()
            val users = rows.map { userCsvParser.parseRowToUser(it) }
            return Result.success(users)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong in csv file"))
        }
    }

    override fun getUserByName(userName: String): Result<UserDto?> {
        return try {
            val user = getAllUsers().getOrThrow().firstOrNull { userName == it.username }
            Result.success(user)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong"))
        }
    }
}