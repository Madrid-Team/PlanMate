package domain.repository

import domain.models.authentication.User

interface UserRepository {
    suspend fun deleteUserByUserId(userId: String)
    suspend fun createNewUser(user: User)
    suspend fun getUserByUserId(userId: String): User
    suspend fun getAllUsers(): List<User>
    suspend fun getUserByName(userName: String): User
    suspend fun login(username: String, password: String): User
}