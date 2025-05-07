package data.source.user

import domain.models.authentication.User

interface UserDataSource {
    fun createNewUser(user: String)
    fun deleteUser(userId: String)
    fun getUserById(userId: String): User?
    fun getAllUsers(): List<User>
    fun getUserByName(userName: String): User?
}