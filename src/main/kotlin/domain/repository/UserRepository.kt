package domain.repository

import domain.models.authentication.User

interface UserRepository {
    fun deleteUser(userId: String)
    fun addUser(user: User)
    fun getUser(userId: String): User?
    fun loginUser(userName: String, password: String):Boolean
}