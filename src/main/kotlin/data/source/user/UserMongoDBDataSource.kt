package org.madrid.data.source.user

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.source.user.ExternalUserDataSource
import domain.models.authentication.User

class UserMongoDBDataSource(
    private val collection: MongoCollection<UserDto>
): ExternalUserDataSource{
    override suspend fun createNewUser(user: User) {

    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(userId: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByName(userName: String): User {
        TODO("Not yet implemented")
    }
}