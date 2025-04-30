package data.source.user

import data.dto.authentication.User

interface UserDataSource {
    fun createUser(user: User):Result<Unit>
    fun deleteUser(userId: String):Result<Unit>
    fun getUser(userId: String):Result<User?>
}