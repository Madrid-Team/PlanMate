package data.source.user

import domain.models.authentication.User

interface UserDataSource {
    fun createNewUser(user: String): Result<Unit>
    fun deleteUser(userId: String)
    fun getUserById(userId: String): Result<User?>
    fun getAllUsers(): Result<List<User>>
    fun getUserByName(userName: String): Result<User?>
}