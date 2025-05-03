package domain.repository

import domain.models.authentication.User

interface UserRepository {
    fun deleteUser(userId: String): Result<Unit>
    fun createNewUser(user: User): Result<Unit>
    fun getUserById(userId: String): Result<User?>
    fun getAllUsers(): Result<List<User>>
    fun getUserByName(userName: String): Result<User?>
}