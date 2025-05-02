package domain.repository

import data.dto.authentication.UserDto

interface UserRepository {
    fun deleteUser(userId: String)
    fun addUser(user: UserDto): Result<Unit>
    fun getUser(userId: String): Result<UserDto?>
    fun getAllUsers(): Result<List<UserDto>>
    fun getUserByName(userName: String):  Result<UserDto?>
}