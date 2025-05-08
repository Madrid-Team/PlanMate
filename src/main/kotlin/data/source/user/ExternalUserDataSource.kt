package data.source.user

import domain.models.authentication.User

interface ExternalUserDataSource {
    suspend fun createNewUser(user: User)
    suspend fun deleteUser(userId: String)
    suspend fun getUserById(userId: String): User
    suspend fun getAllUsers(): List<User>
    suspend fun getUserByName(userName: String): User
}