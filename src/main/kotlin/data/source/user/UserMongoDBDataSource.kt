package org.madrid.data.source.user

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.dto.project.ProjectDto
import data.source.user.ExternalUserDataSource
import domain.models.authentication.User

class UserMongoDBDataSource(
    private val collection: MongoCollection<UserDto>
): ExternalUserDataSource{
    override fun createNewUser(user: User) {

    }

    override fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

    override fun getUserById(userId: String): User {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override fun getUserByName(userName: String): User {
        TODO("Not yet implemented")
    }
}