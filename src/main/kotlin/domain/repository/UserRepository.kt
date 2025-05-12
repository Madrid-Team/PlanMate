package domain.repository

import data.dto.authentication.UserDto
import domain.models.authentication.User

interface UserRepository {
    suspend fun deleteUser(userId: String)
    suspend fun createNewUser(user: User)
    suspend fun getUserById(userId: String): User
    suspend fun getAllUsers(): List<User>
    suspend fun getUserByName(userName: String): User
    suspend fun login(username: String, password: String): User
}