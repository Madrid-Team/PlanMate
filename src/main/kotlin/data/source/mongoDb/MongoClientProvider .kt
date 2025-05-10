package org.madrid.data.source.mongoDb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.madrid.data.utils.DATABASE_NAME

class MongoClientProvider {

    private val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    private val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(System.getenv("ASHRAF_MONGO_URL")))
        .serverApi(serverApi)
        .build()

    private val mongoClient = MongoClient.create(mongoClientSettings)

    fun getDatabase() = mongoClient.getDatabase(DATABASE_NAME)

    fun close() {
        mongoClient.close() // Safely closes all resources
    }
}
