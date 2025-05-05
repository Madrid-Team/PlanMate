package org.madrid.data.source.mongoDb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient

class MongoClientConnection {
    val connectionString =
        "mongodb+srv://madrid:madrid123@cluster0.pskm34b.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
    val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .serverApi(serverApi)
        .build()

    val mongoClient = MongoClient.create(mongoClientSettings)

    fun getDatabase() = mongoClient.getDatabase("planmate")
}