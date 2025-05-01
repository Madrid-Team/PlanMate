package data.source.user

import data.dto.authentication.UserDto
import data.utils.FileCsvReader
import data.utils.FileCsvWriter
import domain.utlis.UserException

class UserCsvDataSource(
    private val fileCsvReader: FileCsvReader,
    private val fileCsvWriter: FileCsvWriter,
    private val userCsvParser: UserCsvParser
): UserDataSource {
    override fun createUser(user: UserDto): Result<Unit> {

        val users : List<UserDto>? =getAllUsers().getOrNull() ?: emptyList()
        val userFound=  users?.find {  it.username == user.username }


        return  if(userFound != null) {
            Result.failure(UserException.UserExist("User already Exist"))

        }
        else {
            val row = userCsvParser.parseUserToRow(user)
            fileCsvWriter.writeToCsvFile(row)
            Result.success(Unit)
        }

    }

    override fun deleteUser(userId: String): Result<Unit> {
        TODO("Not yet implemented")
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
            val users = rows.map { userCsvParser.parseRowToUser(it)}
            return Result.success(users)
        } catch (_ : Exception) {
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