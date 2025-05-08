package data.repository

import com.google.common.truth.Truth.assertThat
import data.source.user.ExternalUserDataSource
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

    private lateinit var externalUserDataSource: ExternalUserDataSource
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        externalUserDataSource = mockk()
        userRepositoryImpl = UserRepositoryImpl(externalUserDataSource)
    }

    @Test
    fun `Should add user successfully`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "hash", "ADMIN")

        every { externalUserDataSource.createNewUser(user) } returns Unit

        // When/Then - No exception is thrown
        // Verify
        assertDoesNotThrow {
            userRepositoryImpl.createNewUser(user)
        }
        verify { externalUserDataSource.createNewUser(user) }
    }

    @Test
    fun `Should fail to add user when datasource fails`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
        val exception = UserExist()
        every { externalUserDataSource.createNewUser(user) } throws exception

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

        every { externalUserDataSource.getUserById("1") } returns user

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

        every { externalUserDataSource.getAllUsers() } returns users

        // When
        val result = userRepositoryImpl.getAllUsers()

        // Then
        assertThat(result).containsExactlyElementsIn(users)
    }

    @Test
    fun `getUserByName should return user when found`() {
        // Given
        val user = User(id = UUID.randomUUID(), "username1", "passwordhash1", "MATE")

        every { externalUserDataSource.getUserByName("username1") } returns user

        // When
        val result = userRepositoryImpl.getUserByName("username1")

        // Then
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `Should delete user successfully`() {
        // Given
        val userId = "1"
        every { externalUserDataSource.deleteUser(userId) } returns Unit

        // When
        userRepositoryImpl.deleteUser(userId)

        // Then - verify the call was made to the data source
        verify { externalUserDataSource.deleteUser(userId) }
    }

    @Test
    fun `Should propagate exception from data source when deleting user`() {
        // Given
        val userId = "2"
        val exception = UserExceptions.UserNotFoundException()
        every { externalUserDataSource.deleteUser(userId) } throws exception

        // When/Then
        val thrownException = assertThrows<UserExceptions.UserNotFoundException> {
            userRepositoryImpl.deleteUser(userId)
        }

        assertThat(thrownException).isSameInstanceAs(exception)
    }
}