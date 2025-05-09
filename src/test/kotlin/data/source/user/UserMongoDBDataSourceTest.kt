package data.source.user

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.CopyCollectionIfDifferentToTest
import data.dto.authentication.UserDto
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.utlis.UserExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.madrid.data.source.mongoDb.MongoClientProvider
import org.madrid.data.source.user.UserMongoDBDataSource
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMongoDBDataSourceTest {
    private lateinit var mongoClientProvider: MongoClientProvider
    private lateinit var database: MongoDatabase
    private lateinit var userMongoDBDataSource: ExternalUserDataSource
    private lateinit var copyCollectionIfDifferentToTest: CopyCollectionIfDifferentToTest
    private lateinit var userDtoCollection: MongoCollection<UserDto>
    private var testScope: TestScope = TestScope()

    @BeforeAll
    fun setup() {
        mongoClientProvider = MongoClientProvider()
        database = mongoClientProvider.getDatabase()
        copyCollectionIfDifferentToTest = CopyCollectionIfDifferentToTest(database, "user_test", "users")
        testScope.launch {
            copyCollectionIfDifferentToTest.copyCollectionIfDifferent()
            userDtoCollection = database.getCollection<UserDto>("user_test")
            userMongoDBDataSource =
                UserMongoDBDataSource(userDtoCollection)

        }

    }

    val id = UUID.randomUUID().toString()
    val userDto = UserDto(
        id = id,
        username = "User Name",
        passwordHash = "hashed_pw",
        role = "admin"
    )
    private val userTest = UserDto(
        id = UUID.randomUUID().toString(),
        username = "User Name",
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
    fun `createNewUser should call insertOne with correct project`() {
        testScope.launch {
            // Arrange
            val collection = mockk<MongoCollection<UserDto>>()


            coEvery {
                collection.insertOne(eq(userTest), any<InsertOneOptions>())
            } returns mockk()

            val dataSource = UserMongoDBDataSource(userDtoCollection)

            dataSource.createNewUser(userTest)
            testScope.launch {
                coVerify(exactly = 1) {
                    collection.insertOne(eq(userTest), any<InsertOneOptions>())
                }
            }
        }
    }


    @Test
    fun `createNewUser adds a project and getAllUsers returns it`() {
        testScope.launch {

            var users: List<UserDto> = emptyList()

            userMongoDBDataSource.createNewUser(userTest2)

            users = userMongoDBDataSource.getAllUsers()

            assertEquals(true, users.any { it.username == "User Name5" })

        }
    }

    @Test
    fun `deleteProject should delete project with given projectId`() {
        testScope.launch {
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
    fun `deleteUser should call deleteOne with the correct filter`() {
        testScope.launch {
            // Given
            val userId = "ghost2"

            val collection = mockk<MongoCollection<User>>()
            val dataSource = UserMongoDBDataSource(userDtoCollection)

            coEvery {
                collection.deleteOne(eq(Filters.eq("_id", "ghost2")), any())
            } returns DeleteResult.acknowledged(1)


            //When
            dataSource.deleteUser(userId)
            testScope.launch {
                //Then
                coVerify {
                    collection.deleteOne(eq(Filters.eq("_id", "ghost2")), any())
                }
            }
        }
    }

    @Test
    fun `getUserById returns user when found`() {
        val collection = mockk<MongoCollection<UserDto>>()
        val dataSource = UserMongoDBDataSource(collection)

        coEvery { collection.find(eq("_id", id)) } returns mockk {
            coEvery { firstOrNull() } returns userDto
        }
        testScope.launch {
            val result = dataSource.getUserById(id)
            assertNotNull(result)
        }
    }

    @Test
    fun `getUserById throws exception when user not found`() {
        val collection = mockk<MongoCollection<UserDto>>()
        val dataSource = UserMongoDBDataSource(collection)

        coEvery { collection.find(eq("_id", "user51")) } returns mockk {
            coEvery { firstOrNull() } returns null
        }
        testScope.launch {
            assertFailsWith<UserExceptions.UserNotFoundException> {
                dataSource.getUserById("user51")
            }
        }
    }

    @Test
    fun `getUserByName returns user when user exists`() {
        testScope.launch {
            // Arrange
            val collection = mockk<MongoCollection<UserDto>>()

            val userName = userTest.username

            val mockFlow = mockk<FindFlow<UserDto>>(relaxed = true)
            coEvery { mockFlow.firstOrNull() } returns userTest
            coEvery { collection.find(eq("username", userName)) } returns mockFlow

            // Act
            copyCollectionIfDifferentToTest.copyCollectionIfDifferent()
            userMongoDBDataSource.createNewUser(userTest)
            val result = userMongoDBDataSource.getUserByName(userName)

            // Assert
            assertEquals(userTest, result)
            coVerify { collection.find(eq("username", userName)) }
            coVerify { mockFlow.firstOrNull() }

        }
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
    fun `getUserById throws exception on database error`() {
        testScope.launch {
            // Arrange
            val collection = mockk<MongoCollection<UserDto>>()
            val userId = "user1"
            val mockFlow = mockk<FindFlow<UserDto>>(relaxed = true)
            coEvery { mockFlow.firstOrNull() } throws RuntimeException("DB error")
            coEvery { collection.find(eq("_id", userId)) } returns mockFlow

            // Act & Assert

            userMongoDBDataSource.getUserById(userId)

                coVerify { collection.find(eq("_id", userId)) }
                coVerify { mockFlow.firstOrNull() }

        }
    }

}