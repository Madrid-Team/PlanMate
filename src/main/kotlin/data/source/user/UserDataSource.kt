package data.source.user

import data.dto.authentication.UserDto
import domain.models.authentication.User

interface UserDataSource {
    fun createNewUser(user: String): Result<Unit>
    fun deleteUser(userId: String): Result<Unit>
    fun getUserById(userId: String): Result<User?>
    fun getAllUsers(): Result<List<User>>
    fun getUserByName(userName: String): Result<User?>
}