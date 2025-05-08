import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.madrid.data.source.mongoDb.MongoClientProvider
import org.madrid.data.utils.DATABASE_NAME


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class MongoClientProviderTest {

    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase

    @BeforeAll
    fun setup() {
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
    }

    @Test
    fun `Should provide a valid MongoDatabase instance When connection is correct and database is returned`() {
        assertNotNull(database)
        assertEquals(DATABASE_NAME, database.name)
    }

    @Test
    fun `Should respond to ping command`() {
        runBlocking {

            val result: Document =
                assertDoesNotThrow { database.runCommand(Document("ping", 1)) }

            assertEquals(1, result.getInteger("ok"))

        }
    }

}
