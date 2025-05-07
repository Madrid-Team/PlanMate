package domain.repository

import domain.models.authentication.User

interface UserRepository {
    fun deleteUser(userId: String)
    fun createNewUser(user: User)
    fun getUserById(userId: String): User?
    fun getAllUsers(): List<User>
    fun getUserByName(userName: String): User?
}