package org.madrid.data.source.user

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.mapper.toDomain
import data.mapper.toDto
import data.source.user.ExternalUserDataSource
import domain.models.authentication.User
import domain.utlis.ProjectExceptions
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class UserMongoDBDataSource(
    private val collection: MongoCollection<UserDto>
): ExternalUserDataSource{
    override suspend fun createNewUser(user: User) {
        collection.insertOne(user.toDto())
    }

    override suspend fun deleteUser(userId: String) {
        val query = eq("_id", userId)
        collection.deleteOne(query)
    }

    override suspend fun getUserById(userId: String): User {
        val filter = eq("_id", userId)
        return collection.find(filter).firstOrNull()?.toDomain() ?: throw ProjectExceptions.ProjectNotFoundException()
    }

    override suspend fun getAllUsers(): List<User> {
        return collection.find().toList().map { it.toDomain() }
    }

    override suspend fun getUserByName(userName: String): User {
        val filter = eq("_id", userName)
        return collection.find(filter).firstOrNull()?.toDomain() ?: throw ProjectExceptions.ProjectNotFoundException()
    }
}