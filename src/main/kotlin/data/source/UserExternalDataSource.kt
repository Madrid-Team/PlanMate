package data.source

import data.dto.authentication.UserDto

interface UserExternalDataSource {
    suspend fun createNewUser(user: UserDto)
    suspend fun deleteUser(userId: String)
    suspend fun getUserById(userId: String): UserDto?
    suspend fun getAllUsers(): List<UserDto>
    suspend fun getUserByName(username: String): UserDto?
    suspend fun login(username: String, password: String): UserDto?
}