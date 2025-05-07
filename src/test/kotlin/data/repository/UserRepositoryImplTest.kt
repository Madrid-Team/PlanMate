package data.repository

import com.google.common.truth.Truth.assertThat
import data.mapper.toDto
import data.source.user.UserCsvParser
import data.source.user.UserDataSource
import domain.models.authentication.User
import domain.utlis.UserExceptions.NotFoundUser
import domain.utlis.UserExceptions.UserExist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class UserRepositoryImplTest {

    private lateinit var userDataSource: UserDataSource
    private lateinit var userCsvParser: UserCsvParser
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userDataSource = mockk()
        userCsvParser = mockk()
        userRepositoryImpl = UserRepositoryImpl(userDataSource, userCsvParser)
    }

    @Test
    fun `Should add user successfully`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "hash", "ADMIN")
        val userRow = "1,username1,hash,ADMIN"

        every { userCsvParser.parseUserToRow(user.toDto()) } returns userRow
        every { userDataSource.createNewUser(userRow) } returns Unit

        // When/Then - No exception is thrown
        userRepositoryImpl.createNewUser(user)

        // Verify
        verify { userDataSource.createNewUser(userRow) }
    }

    @Test
    fun `Should fail to add user when datasource fails`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
        val userRow = "2,username2,hash2,MATE"
        val exception = UserExist("User already exists")

        every { userCsvParser.parseUserToRow(user.toDto()) } returns userRow
        every { userDataSource.createNewUser(userRow) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            userRepositoryImpl.createNewUser(user)
        }

        // Verify exception is wrapped
        assertThat(thrownException.cause).isSameInstanceAs(exception)
    }

    @Test
    fun `getUser should return user when found`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "passwordhash1", "MATE")

        every { userDataSource.getUserById("1") } returns user

        // When
        val result = userRepositoryImpl.getUserById("1")

        // Then
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `getAllUsers should return user when found`() {
        // Given
        val users = listOf(
            User(id = UUID.randomUUID(), "username1", "hash1", "MATE"),
            User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
        )

        every { userDataSource.getAllUsers() } returns users

        // When
        val result = userRepositoryImpl.getAllUsers()

        // Then
        assertThat(result).containsExactlyElementsIn(users)
    }

    @Test
    fun `getUserByName should return user when found`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "passwordhash1", "MATE")

        every { userDataSource.getUserByName("username1") } returns user

        // When
        val result = userRepositoryImpl.getUserByName("username1")

        // Then
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `Should delete user successfully`() {
        // Given
        val userId = "1"
        every { userDataSource.deleteUser(userId) } returns Unit

        // When
        userRepositoryImpl.deleteUser(userId)

        // Then - verify the call was made to the data source
        verify { userDataSource.deleteUser(userId) }
    }

    @Test
    fun `Should propagate exception from data source when deleting user`() {
        // Given
        val userId = "2"
        val exception = NotFoundUser("User not found")
        every { userDataSource.deleteUser(userId) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            userRepositoryImpl.deleteUser(userId)
        }

        assertThat(thrownException.cause).isSameInstanceAs(exception)
    }
}