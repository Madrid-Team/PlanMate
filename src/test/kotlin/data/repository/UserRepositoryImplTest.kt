package data.repository

import com.google.common.truth.Truth.assertThat
import data.mapper.toDto
import data.source.user.UserExternalDataSource
import domain.models.authentication.User
import domain.utlis.UserExceptions
import domain.utlis.UserExceptions.UserExist
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class UserRepositoryImplTest {

    private lateinit var userExternalDataSource: UserExternalDataSource
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userExternalDataSource = mockk()
        userRepositoryImpl = UserRepositoryImpl(userExternalDataSource)
    }

    @Test
    fun `Should add user successfully`() {
        runTest {
            // Given
            val user = User(id = UUID.randomUUID(), "username1", "hash", "ADMIN")

            coEvery { userExternalDataSource.createNewUser(user.toDto()) } returns Unit

            // When/Then - No exception is thrown
            // Verify
            assertDoesNotThrow {
                userRepositoryImpl.createNewUser(user)
            }
            coVerify { userExternalDataSource.createNewUser(user.toDto()) }
        }
    }

    @Test
    fun `Should fail to add user when datasource fails`() {
        runTest {
            // Given
            val user = User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
            val exception = UserExist()
            coEvery { userExternalDataSource.createNewUser(user.toDto()) } throws exception

            // When/Then
            val thrownException = assertThrows<UserExist> {
                userRepositoryImpl.createNewUser(user)
            }

            // Verify exception is wrapped
            assertThat(thrownException).isSameInstanceAs(exception)
        }
    }

    @Test
    fun `getUser should return user when found`() {
        runTest {
            // Given
            val user = User(id = UUID.randomUUID(), "username1", "passwordhash1", "MATE")

            coEvery { userExternalDataSource.getUserById("1") } returns user.toDto()

            // When
            val result = userRepositoryImpl.getUserById("1")

            // Then
            assertThat(result).isEqualTo(user)
        }
    }

    @Test
    fun `getAllUsers should return user when found`() {
        runTest {
            // Given
            val users = listOf(
                User(id = UUID.randomUUID(), "username1", "hash1", "MATE"),
                User(id = UUID.randomUUID(), "username2", "hash2", "MATE")
            )

            coEvery { userExternalDataSource.getAllUsers() } returns users.map { it.toDto() }

            // When
            val result = userRepositoryImpl.getAllUsers()

            // Then
            assertThat(result).containsExactlyElementsIn(users)
        }
    }

    @Test
    fun `getUserByName should return user when found`() {
        runTest {
            // Given
            val user = User(id = UUID.randomUUID(), "username1", "passwordhash1", "MATE")

            coEvery { userExternalDataSource.getUserByName("username1") } returns user.toDto()

            // When
            val result = userRepositoryImpl.getUserByName("username1")

            // Then
            assertThat(result).isEqualTo(user)
        }
    }

    @Test
    fun `Should delete user successfully`() {
        runTest {
            // Given
            val userId = "1"
            coEvery { userExternalDataSource.deleteUser(userId) } returns Unit

            // When
            userRepositoryImpl.deleteUser(userId)

            // Then - verify the call was made to the data source
            coVerify { userExternalDataSource.deleteUser(userId) }
        }
    }

    @Test
    fun `Should propagate exception from data source when deleting user`() {
        runTest {
            // Given
            val userId = "2"
            val exception = UserExceptions.UserNotFoundException()
            coEvery { userExternalDataSource.deleteUser(userId) } throws exception

            // When/Then
            val thrownException = assertThrows<UserExceptions.UserNotFoundException> {
                userRepositoryImpl.deleteUser(userId)
            }

            assertThat(thrownException).isSameInstanceAs(exception)
        }
    }

}