package data.source.user

import domain.models.authentication.User

interface ExternalUserDataSource {
    fun createNewUser(user: User)
    fun deleteUser(userId: String)
    fun getUserById(userId: String): User
    fun getAllUsers(): List<User>
    fun getUserByName(userName: String): User
}