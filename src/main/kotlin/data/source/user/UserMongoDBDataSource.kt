package org.madrid.data.source.user

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.source.user.ExternalUserDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class UserMongoDBDataSource(
    private val collection: MongoCollection<UserDto>
): ExternalUserDataSource{
    override suspend fun createNewUser(user: UserDto) {
        collection.insertOne(user)
    }

    override suspend fun deleteUser(userId: String) {
        val query = eq("_id", userId)
        collection.deleteOne(query)
    }

    override suspend fun getUserById(userId: String): UserDto? {
        val filter = eq("_id", userId)
        return collection.find(filter).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return collection.find().toList()
    }

    override suspend fun getUserByName(userName: String): UserDto? {
        val filter = eq("name", userName)
        return collection.find(filter).firstOrNull()
    }
}