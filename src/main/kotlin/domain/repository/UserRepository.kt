package domain.repository

import domain.models.authentication.User

interface UserRepository {
    fun deleteUser(userId: String)
    fun createNewUser(user: User): Result<Unit>
    fun getUserById(userId: String): Result<User?>
    fun getAllUsers(): Result<List<User>>
    fun getUserByName(userName: String): Result<User?>
}