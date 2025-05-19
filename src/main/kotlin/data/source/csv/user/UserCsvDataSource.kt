package data.source.csv.user

import data.dto.authentication.UserDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.utils.UserExist
import domain.utils.UserNotFoundException

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
) : UserExternalDataSource {
    override suspend fun createNewUser(user: UserDto) {
        val row: String = userCsvParser.parseUserToRow(user)
        try {
            getUserByName(user.username)
            throw UserExist()
        } catch (_: UserNotFoundException) {
            fileCsvWriter.writeToCsvFile(row)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteUser(userId: String) {
        val allUsers = getAllUsers()
        if (allUsers.isEmpty()) throw UserNotFoundException()
        val updatedUsers = allUsers.filter { it.id != userId }
        val userRows = updatedUsers.map { user ->
            userCsvParser.parseUserToRow(user)
        }
        fileCsvWriter.updateCsvFile(if (userRows.isEmpty()) "" else userRows.joinToString("\n"))
    }

    override suspend fun getUserById(userId: String): UserDto? {
        return getAllUsers().firstOrNull { userId == it.id }

    }

    override suspend fun getAllUsers(): List<UserDto> {
        val rows = fileCsvReader.readCsvFile()
        val users = rows.map { userCsvParser.parseRowToUser(it)}
        return users

    }

    override suspend fun getUserByName(username: String): UserDto? {
        return getAllUsers().firstOrNull { username == it.username }
    }

    override suspend fun login(username: String, password: String): UserDto? {
        TODO("Not yet implemented")
    }
}