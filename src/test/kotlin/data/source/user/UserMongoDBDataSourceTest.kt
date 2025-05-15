package data.source.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.authentication.UserDto
import data.source.csv.user.UserExternalDataSource
import data.source.mongoDb.UserMongoDBDataSource
import data.source.mongoDb.MongoClientProvider
import data.utils.PASSWORD
import data.utils.USER_NAME
import domain.models.authentication.UserRole
import domain.utils.UserExceptions
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.bson.Document
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds


class UserMongoDBDataSourceTest {
    private var mongoClientProvider: MongoClientProvider = MongoClientProvider()
    private var database: MongoDatabase = mongoClientProvider.getDatabase()
    private lateinit var userMongoDBDataSource: UserExternalDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    private lateinit var userDtoCollection: MongoCollection<UserDto>
    private var testScope: TestScope = TestScope()

    @BeforeEach
    fun setup() {
        //  Dispatchers.setMain(TestDispatcher())
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database,  "users")
        runTest {
            copyCollectionIfDifferentToTest.copyCollectionIfDifferent()
        }
        userDtoCollection = database.getCollection<UserDto>("Collection_Test")
        userMongoDBDataSource =
            UserMongoDBDataSource(userDtoCollection)
//        advanceUntilIdle()

    }

    val id = UUID.randomUUID().toString()

    private val userTest = UserDto(
        id = "AbdoId",
        username = "User Name Abdo",
        passwordHash = "shci58392nwsuss9203asdx",
        role = UserRole.ADMIN.name,
    )
    private val userTest2 = UserDto(
        id = UUID.randomUUID().toString(),
        username = "User Name5",
        passwordHash = "shci58392nwsuss9203asdxh",
        role = UserRole.ADMIN.name,
    )


    @Test
    fun `createNewUser adds a user and getAllUsers returns it`() {
        runTest {

            var users: List<UserDto> = emptyList()

            userMongoDBDataSource.createNewUser(userTest2)

            users = userMongoDBDataSource.getAllUsers()

            assertEquals(true, users.any { it.username == "User Name5" })

        }
    }

    @Test
    fun `deleteProject should delete project with given projectId`() {
        testScope.runTest {
            //Given
            val userId = userMongoDBDataSource.getAllUsers().first().id
            val userListSizeBeforeDelete = userMongoDBDataSource.getAllUsers().size

            // When
            userMongoDBDataSource.deleteUser(userId)

            // Then
            assertEquals(userListSizeBeforeDelete - 1, userMongoDBDataSource.getAllUsers().size)

        }
    }

    @Test
    fun `deleteUser should delete user with correct id`() = runTest(timeout = 60.seconds) {
        // Arrange
        val userTest = UserDto(
            id = "ghost2",
            username = "GhostUser",
            passwordHash = "shci58392nwsuss9203asdx",
            role = "ADMIN"
        )
        userMongoDBDataSource.createNewUser(userTest)

        // Act
        userMongoDBDataSource.deleteUser(userTest.id)

        // Assert
        val result = userMongoDBDataSource.getUserByName(userTest.username)
        assertNull(result, "User should be deleted")
        // Alternative: Directly check collection
        val deletedUser = userDtoCollection.find(eq("_id", userTest.id)).firstOrNull()
        assertNull(deletedUser, "User should not exist in collection")
    }

    @Test
    fun `deleteUser should call deleteOne with the correct filter`() = runTest {
        // Given
        val collection = mockk<MongoCollection<UserDto>>()
        val dataSource = UserMongoDBDataSource(collection)
        val insertOneResult = mockk<InsertOneResult>()
        coEvery { insertOneResult.wasAcknowledged() } returns true
        coEvery { collection.insertOne(userTest2, any()) } returns insertOneResult
        coEvery {
            collection.deleteOne(
                eq(Filters.eq("_id", userTest2.id)),
                any()
            )
        } returns DeleteResult.acknowledged(1)

        // When
        dataSource.createNewUser(userTest2)
        dataSource.deleteUser(userTest2.id)

        // Then
        coVerify(exactly = 1) { collection.insertOne(userTest2, any()) }
        coVerify(exactly = 1) { collection.deleteOne(eq(Filters.eq("_id", userTest2.id)), any()) }
    }

    @Test
    fun `getUserById returns user when found`() = runTest {
        userDtoCollection.insertOne(userTest)

        userDtoCollection.find(eq("_id", userTest.id))


        val result = assertDoesNotThrow {
            userMongoDBDataSource.getUserById(userTest.id)
        }
        assertNotNull(result)

    }


    @Test
    fun `getUserById throws exception when user not found`() = runTest {
        // Arrange
        val collection = mockk<MongoCollection<UserDto>>(relaxed = true)
        val dataSource = UserMongoDBDataSource(collection)
        val mockFlow = mockk<FindFlow<UserDto>>(relaxed = true)
        // Explicitly match Filters.eq
        coEvery { collection.find<UserDto>(Filters.eq("_id", "user51"), any()) } returns mockFlow
        coEvery { mockFlow.firstOrNull() } returns null


        // Act & Assert
        assertNull(dataSource.getUserById("mo"))

    }

    @Test
    fun `test getUserByName when user exists`() = runTest {
        // Given: Insert a user into the real MongoDB server

        userDtoCollection.insertOne(userTest)

        // When: Fetch the user by username
        val result = userMongoDBDataSource.getUserByName(userTest.username)

        // Then
        assertNotNull(result)
        assertEquals("AbdoId", result.id)
        assertEquals("User Name Abdo", result.username)
    }


    @Test
    fun `getUserByName throws exception when user not found`() {
        val collection = mockk<MongoCollection<UserDto>>()
        val dataSource = UserMongoDBDataSource(collection)

        coEvery { collection.find(eq("_id", "User Name")) } returns mockk {
            coEvery { firstOrNull() } returns null
        }
        testScope.launch {
            assertFailsWith<UserExceptions.UserNotFoundException> {
                dataSource.getUserByName("User Name")
            }
        }
    }

    @Test
    fun `login returns user when credentials match`() = runTest {
        // Given
        val username = "testUser"
        val password = "pass123"

        val expectedUser = UserDto("1", username, password, "Admin")
        userMongoDBDataSource.createNewUser(expectedUser)

        val filter = and(eq(USER_NAME, username), eq(PASSWORD, password))


        assertEquals(userDtoCollection.find(filter).firstOrNull(), expectedUser)

        // When
        val result = userMongoDBDataSource.login(username, password)

        // Then
        assertEquals(expectedUser, result)

    }

    @Test
    fun `login returns null when credentials do not match`() = runTest {
        // Given
        val username = "wrongUser"
        val password = "wrongPass"
        val filter = and(eq("username", username), eq("password", password))


        assertNull(userDtoCollection.find(filter).firstOrNull())


        // When
        val result = userMongoDBDataSource.login(username, password)

        // Then
        assertNull(result)


    }


    @AfterEach
    fun cleanup() {
        runTest {
            userDtoCollection.deleteMany(Document())
            clearAllMocks()

        }
        mongoClientProvider.close()

    }
}