package domain.repository

import domain.models.authentication.User

interface UserRepository {
    suspend fun deleteUser(userId: String)
    suspend fun createNewUser(user: User)
    suspend fun getUserById(userId: String): User
    suspend fun getAllUsers(): List<User>
    suspend fun getUserByName(userName: String): User
}