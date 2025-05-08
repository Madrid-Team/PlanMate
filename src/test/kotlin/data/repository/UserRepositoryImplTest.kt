package data.repository

import com.google.common.truth.Truth.assertThat
import data.source.user.UserDataSource
import domain.models.authentication.User
import domain.utlis.UserExceptions
import domain.utlis.UserExceptions.UserExist
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class UserRepositoryImplTest {

    private lateinit var userDataSource: UserDataSource
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userDataSource = mockk()
        userRepositoryImpl = UserRepositoryImpl(userDataSource)
    }

    @Test
    fun `Should add user successfully`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "hash", "ADMIN")

        every { userDataSource.createNewUser(user) } returns Unit

        // When/Then - No exception is thrown
        // Verify
        assertDoesNotThrow {
            userRepositoryImpl.createNewUser(user)
        }
        verify { userDataSource.createNewUser(user) }
    }

    @Test
    fun `Should fail to add user when datasource fails`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
        val exception = UserExist()
        every { userDataSource.createNewUser(user) } throws exception

        // When/Then
        val thrownException = assertThrows<UserExist> {
            userRepositoryImpl.createNewUser(user)
        }

        // Verify exception is wrapped
        assertThat(thrownException).isSameInstanceAs(exception)
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
        val exception = UserExceptions.UserNotFoundException()
        every { userDataSource.deleteUser(userId) } throws exception

        // When/Then
        val thrownException = assertThrows<UserExceptions.UserNotFoundException> {
            userRepositoryImpl.deleteUser(userId)
        }

        assertThat(thrownException).isSameInstanceAs(exception)
    }
}