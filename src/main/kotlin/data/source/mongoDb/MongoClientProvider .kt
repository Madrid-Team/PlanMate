package org.madrid.data.source.mongoDb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.madrid.data.utils.CONNECTION_STRING
import org.madrid.data.utils.DATABASE_NAME

class MongoClientProvider {

    private val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    private val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(CONNECTION_STRING))
        .serverApi(serverApi)
        .build()

    private val mongoClient = MongoClient.create(mongoClientSettings)

    val database: MongoDatabase by lazy {
        mongoClient.getDatabase(DATABASE_NAME)
    }
}
