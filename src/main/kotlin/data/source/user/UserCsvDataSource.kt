package data.source.user

import data.mapper.toDomain
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.authentication.User
import domain.utlis.UserException

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
) : UserDataSource {
    override fun createNewUser(row: String): Result<Unit> {
        try {
            fileCsvWriter.writeToCsvFile(row)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun deleteUser(userId: String): Result<Unit> {
        val allUsers = getAllUsers().getOrThrow()
        val updatedUsers = allUsers.filter { it.id.toString() != userId }

        if (allUsers.size == updatedUsers.size) return Result.failure(UserException.UserNotFoundException)

        val userRows = updatedUsers.map { user ->
            userCsvParser.parseUserToRow(user.toDto())
        }


        fileCsvWriter.updateCsvFile(if (userRows.isEmpty()) "" else userRows.joinToString("\n"))
        return Result.success(Unit)
    }

    override fun getUserById(userId: String): Result<User?> {
        return try {
            val user = getAllUsers().getOrThrow().firstOrNull { userId == it.id.toString() }
            Result.success(user)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong"))
        }
    }

    override fun getAllUsers(): Result<List<User>> {
        return try {
            val rows = fileCsvReader.readCsvFile()
            val users = rows.map { userCsvParser.parseRowToUser(it).toDomain() }
            return Result.success(users)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong in csv file"))
        }
    }

    override fun getUserByName(userName: String): Result<User?> {
        return try {
            val user = getAllUsers().getOrThrow().firstOrNull { userName == it.username }
            Result.success(user)
        } catch (_: Exception) {
            Result.failure(Exception("Something went wrong"))
        }
    }
}