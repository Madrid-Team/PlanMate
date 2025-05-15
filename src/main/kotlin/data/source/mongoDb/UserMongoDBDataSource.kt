package data.source.mongoDb

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.authentication.UserDto
import data.source.csv.user.UserExternalDataSource
import data.utils.PASSWORD
import data.utils.USER_ID
import data.utils.USER_NAME
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class UserMongoDBDataSource(
    private val collection: MongoCollection<UserDto>
): UserExternalDataSource {
    override suspend fun createNewUser(user: UserDto) {
        collection.insertOne(user)
    }

    override suspend fun deleteUser(userId: String) {
        val query = Filters.eq(USER_ID, userId)
        collection.deleteOne(query)
    }

    override suspend fun getUserById(userId: String): UserDto? {
        val filter = Filters.eq(USER_ID, userId)
        return collection.find(filter).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return collection.find().toList()
    }

    override suspend fun login(username: String, password: String): UserDto? {
        val filter = Filters.and(
            Filters.eq(USER_NAME, username),
            Filters.eq(PASSWORD, password)
        )
        return collection.find(filter).firstOrNull()
    }

    override suspend fun getUserByName(username: String): UserDto? {
        val filter = Filters.eq(USER_NAME, username)
        return collection.find(filter).firstOrNull()
    }
}