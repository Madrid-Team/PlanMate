package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        // Given
        val adminId = UUID.randomUUID().toString()
        val userToDeleteId = UUID.randomUUID().toString()
        val adminUser = User(id = UUID.randomUUID(), "admin", "hash", "ADMIN")

        every { userRepository.getUserById(adminId) } returns adminUser
        every { userRepository.getUserById(userToDeleteId) } returns User(
            id = UUID.randomUUID(),
            "userToDelete",
            "hash",
            "MATE"
        )
        every { userRepository.deleteUser(userToDeleteId) } returns Unit

        // When - Should not throw exception
        deleteUserUseCase.invoke(adminId, userToDeleteId)

        // Then
        verify(exactly = 1) { userRepository.getUserById(adminId) }
        verify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
        verify(exactly = 1) { userRepository.deleteUser(userToDeleteId) }
    }

    @Test
    fun `invoke should fail when requestor is not admin`() {
        // Given
        val mateId = UUID.randomUUID().toString()
        val userToDeleteId = UUID.randomUUID().toString()
        val mateUser = User(id = UUID.randomUUID(), "mate", "hash", "MATE")

        every { userRepository.getUserById(mateId) } returns mateUser
        every { userRepository.getUserById(userToDeleteId) } returns User(
            id = UUID.randomUUID(),
            "userToDelete",
            "hash",
            "MATE"
        )

        // When/Then
        val exception = assertThrows<UserExceptions> {
            deleteUserUseCase.invoke(mateId, userToDeleteId)
        }

        assertEquals("User is not an admin", exception.message)
        verify(exactly = 1) { userRepository.getUserById(mateId) }
        verify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `invoke should fail when requestor user not found`() {
        // Given
        val nonExistentId = "non-existent-id"
        val userToDeleteId = "user-to-delete-id"

        every { userRepository.getUserById(nonExistentId) } returns null

        // When/Then
        val exception = assertThrows<UserExceptions.UserNotFoundException> {
            deleteUserUseCase.invoke(nonExistentId, userToDeleteId)
        }

        verify(exactly = 1) { userRepository.getUserById(nonExistentId) }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `invoke should fail when user to delete not found`() {
        // Given
        val adminId = "admin-id"
        val userToDeleteId = "non-existent-id"
        val adminUser = User(id = UUID.randomUUID(), "admin", "hash", "ADMIN")

        every { userRepository.getUserById(adminId) } returns adminUser
        every { userRepository.getUserById(userToDeleteId) } returns null

        // When/Then
        val exception = assertThrows<UserExceptions.UserNotFoundException> {
            deleteUserUseCase.invoke(adminId, userToDeleteId)
        }

        verify(exactly = 1) { userRepository.getUserById(adminId) }
        verify(exactly = 1) { userRepository.getUserById(userToDeleteId) }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `invoke should fail when repository throws exception`() {
        // Given
        val adminId = "admin-id"
        val userToDeleteId = "user-to-delete-id"
        val adminUser = User(id = UUID.randomUUID(), "admin", "hash", "ADMIN")
        val databaseException = Exception("Database error")

        every { userRepository.getUserById(adminId) } returns adminUser
        every { userRepository.getUserById(userToDeleteId) } returns User(
            id = UUID.randomUUID(),
            "userToDelete",
            "hash",
            "MATE"
        )
        every { userRepository.deleteUser(userToDeleteId) } throws databaseException

        // When/Then
        val exception = assertThrows<Exception> {
            deleteUserUseCase.invoke(adminId, userToDeleteId)
        }

        assertEquals(databaseException, exception)
    }
}