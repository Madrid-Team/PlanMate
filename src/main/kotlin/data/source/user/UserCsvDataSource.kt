package data.source.user

import data.mapper.toDomain
import data.mapper.toDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.models.authentication.User

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
) : UserDataSource {
    override fun createNewUser(row: String) {
        fileCsvWriter.writeToCsvFile(row)
    }

    override fun deleteUser(userId: String) {
        val allUsers = getAllUsers()
        val updatedUsers = allUsers.filter { it.id.toString() != userId }
        val userRows = updatedUsers.map { user ->
            userCsvParser.parseUserToRow(user.toDto())
        }
        fileCsvWriter.updateCsvFile(if (userRows.isEmpty()) "" else userRows.joinToString("\n"))
    }

    override fun getUserById(userId: String): User? {
        val user = getAllUsers().firstOrNull { userId == it.id.toString() }
        return user

    }

    override fun getAllUsers(): List<User> {
        val rows = fileCsvReader.readCsvFile()
        val users = rows.map { userCsvParser.parseRowToUser(it).toDomain() }

        return users

    }

    override fun getUserByName(userName: String): User? {
        return getAllUsers().firstOrNull { userName == it.username }
    }
}