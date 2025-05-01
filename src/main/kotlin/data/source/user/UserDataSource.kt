package data.source.user

import data.dto.authentication.UserDto

interface UserDataSource {
    fun createUser(user: UserDto):Result<Unit>
    fun deleteUser(userId: String):Result<Unit>
    fun getUser(userId: String):Result<UserDto?>
    fun getAllUsers():Result<List<UserDto>>
    fun getUserByName(userName: String): Result<UserDto?>
}