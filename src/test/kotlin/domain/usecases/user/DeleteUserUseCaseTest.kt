package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `invoke should delete user when requestor is admin`() {
        runTest {
            // Given
            val adminId = UUID.randomUUID().toString()
            val userToDeleteId = UUID.randomUUID().toString()
            val adminUser = User(id = UUID.randomUUID(), "admin", "hash", "ADMIN")

            coEvery { userRepository.getUserById(adminId) } returns adminUser
            coEvery { userRepository.getUserById(userToDeleteId) } returns User(
                id = UUID.randomUUID(),
                "userToDelete",
                "hash",
                "MATE"
            )
            coEvery { userRepository.deleteUser(userToDeleteId) } returns Unit

            // When - Should not throw exception
            deleteUserUseCase.invoke(adminId, userToDeleteId)

            // Then
            coVerify(exactly = 1) { userRepository.getUserById(adminId) }
            coVerify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
            coVerify(exactly = 1) { userRepository.deleteUser(userToDeleteId) }
        }
    }

    @Test
    fun `invoke should fail when requestor is not admin`() {
        runTest {
        // Given
        val mateId = UUID.randomUUID().toString()
        val userToDeleteId = UUID.randomUUID().toString()
        val mateUser = User(id = UUID.randomUUID(), "mate", "hash", "MATE")

        coEvery { userRepository.getUserById(mateId) } returns mateUser
        coEvery { userRepository.getUserById(userToDeleteId) } returns User(
            id = UUID.randomUUID(),
            "userToDelete",
            "hash",
            "MATE"
        )

        // When/Then
        val exception = assertThrows<UserExceptions> {
            deleteUserUseCase.invoke(mateId, userToDeleteId)
        }

        assertEquals("User is not admin", exception.message)
        coVerify(exactly = 1) { userRepository.getUserById(mateId) }
        coVerify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
        coVerify(exactly = 0) { userRepository.deleteUser(any()) }
        }
    }

    @Test
    fun `invoke should fail when requestor user not found`() {
        runTest {
            // Given
            val nonExistentId = "non-existent-id"
            val userToDeleteId = "user-to-delete-id"

            coEvery { userRepository.getUserById(nonExistentId) } throws UserExceptions.UserNotFoundException()

            // When/Then
            val exception = assertThrows<UserExceptions.UserNotFoundException> {
                deleteUserUseCase.invoke(nonExistentId, userToDeleteId)
            }

            coVerify(exactly = 1) { userRepository.getUserById(nonExistentId) }
            coVerify(exactly = 0) { userRepository.deleteUser(any()) }
            assertThat(exception).isInstanceOf(UserExceptions.UserNotFoundException::class.java)
        }
    }

    @Test
    fun `invoke should fail when user to delete not found`() {
        runTest {
            // Given
            val adminId = "admin-id"
            val userToDeleteId = "non-existent-id"

            coEvery { userRepository.getUserById(userToDeleteId) } throws UserExceptions.UserNotFoundException()

            // When/Then
            val exception = assertThrows<UserExceptions.UserNotFoundException> {
                deleteUserUseCase.invoke(adminId, userToDeleteId)
            }

            coVerify(exactly = 1) { userRepository.getUserById(adminId) }
            coVerify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
            coVerify(exactly = 0) { userRepository.deleteUser(any()) }
            assertThat(exception).isInstanceOf(UserExceptions.UserNotFoundException::class.java)
        }
    }

    @Test
    fun `invoke should fail when repository throws exception`() {
        runTest {
            // Given
            val adminId = "admin-id"
            val userToDeleteId = "user-to-delete-id"
            val adminUser = User(id = UUID.randomUUID(), "admin", "hash", "ADMIN")
            val databaseException = Exception("Database error")

            coEvery { userRepository.getUserById(adminId) } returns adminUser
            coEvery { userRepository.getUserById(userToDeleteId) } returns User(
                id = UUID.randomUUID(),
                "userToDelete",
                "hash",
                "MATE"
            )
            coEvery { userRepository.deleteUser(userToDeleteId) } throws databaseException

            // When/Then
            val exception = assertThrows<Exception> {
                deleteUserUseCase.invoke(adminId, userToDeleteId)
            }

            assertEquals(databaseException, exception)
        }
    }
}