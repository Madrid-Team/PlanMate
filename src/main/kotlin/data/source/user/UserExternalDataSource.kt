package data.source.user

import data.dto.authentication.UserDto

interface UserExternalDataSource {
    suspend fun createNewUser(user: UserDto)
    suspend fun deleteUser(userId: String)
    suspend fun getUserById(userId: String): UserDto?
    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserByName(userName: String): UserDto?
}