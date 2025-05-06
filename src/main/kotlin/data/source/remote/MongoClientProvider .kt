package data.source.remote

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.madrid.data.utils.CONNECTION_STRING
import org.madrid.data.utils.DATABASE_NAME
import org.madrid.data.utils.DatabaseConnectionException

class MongoClientProvider {

    private val mongoClient: MongoClient by lazy {
        try {
            val serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build()

            val mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(CONNECTION_STRING))
                .serverApi(serverApi)
                .build()

            MongoClient.create(mongoClientSettings)
        } catch (exception: Exception) {
            throw DatabaseConnectionException("Failed to create MongoDB client $exception")
        }
    }

    fun getDatabase(): MongoDatabase {
        return try {
            mongoClient.getDatabase(DATABASE_NAME)
        } catch (exception: Exception) {
            throw DatabaseConnectionException("Failed to get database $DATABASE_NAME: $exception")
        }
    }
}
