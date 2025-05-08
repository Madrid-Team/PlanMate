package data.source.user

import data.mapper.toDomain
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.authentication.User
import domain.utlis.UserExceptions

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
) : ExternalUserDataSource {
    override suspend fun createNewUser(user: User) {
        val row: String = userCsvParser.parseUserToRow(user.toDto())
        try {
            getUserByName(user.username)
            throw UserExceptions.UserExist()
        } catch (_: UserExceptions.UserNotFoundException) {
            fileCsvWriter.writeToCsvFile(row)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteUser(userId: String) {
        val allUsers = getAllUsers()
        if (allUsers.isEmpty()) throw UserExceptions.UserNotFoundException()
        val updatedUsers = allUsers.filter { it.id.toString() != userId }
        val userRows = updatedUsers.map { user ->
            userCsvParser.parseUserToRow(user.toDto())
        }
        fileCsvWriter.updateCsvFile(if (userRows.isEmpty()) "" else userRows.joinToString("\n"))
    }

    override suspend fun getUserById(userId: String): User {
        val user = getAllUsers().firstOrNull { userId == it.id.toString() }
        return user ?: throw UserExceptions.UserNotFoundException()

    }

    override suspend fun getAllUsers(): List<User> {
        val rows = fileCsvReader.readCsvFile()
        val users = rows.map { userCsvParser.parseRowToUser(it).toDomain() }

        return users

    }

    override suspend fun getUserByName(userName: String): User {
        return getAllUsers().firstOrNull { userName == it.username } ?: throw UserExceptions.UserNotFoundException()
    }
}