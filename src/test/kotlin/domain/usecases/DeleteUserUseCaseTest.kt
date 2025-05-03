package domain.usecases

import data.dto.authentication.UserDto
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.usecases.user.DeleteUserUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val adminId = "admin-id"
        val userToDeleteId = "user-to-delete-id"
        val adminUser = UserDto(
            id = adminId,
            username = "admin",
            passwordHash = "hash",
            role = UserRole.ADMIN.name
        )

        every { userRepository.getUser(adminId) } returns Result.success(adminUser)
        every { userRepository.deleteUser(userToDeleteId) } returns Result.success(Unit)

        // When
        val result = deleteUserUseCase.invoke(adminId, userToDeleteId)

        // Then
        assertTrue(result.isSuccess)
        verify(exactly = 1) { userRepository.getUser(adminId) }
        verify(exactly = 1) { userRepository.deleteUser(userToDeleteId) }
    }

    @Test
    fun `invoke should fail when requestor is not admin`() {
        // Given
        val mateId = "mate-id"
        val userToDeleteId = "user-to-delete-id"
        val mateUser = UserDto(
            id = mateId,
            username = "mate",
            passwordHash = "hash",
            role = UserRole.MATE.name
        )

        every { userRepository.getUser(mateId) } returns Result.success(mateUser)

        // When
        val result = deleteUserUseCase.invoke(mateId, userToDeleteId)

        // Then
        assertTrue(result.isFailure)
        assertEquals("User is not an admin", result.exceptionOrNull()?.message)
        verify(exactly = 1) { userRepository.getUser(mateId) }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `invoke should fail when requestor user not found`() {
        // Given
        val nonExistentId = "non-existent-id"
        val userToDeleteId = "user-to-delete-id"
        val exception = Exception("User not found")

        every { userRepository.getUser(nonExistentId) } returns Result.failure(exception)

        // When
        val result = deleteUserUseCase.invoke(nonExistentId, userToDeleteId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(exactly = 1) { userRepository.getUser(nonExistentId) }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `invoke should fail when repository throws exception`() {
        // Given
        val adminId = "admin-id"
        val userToDeleteId = "user-to-delete-id"
        val adminUser = UserDto(
            id = adminId,
            username = "admin",
            passwordHash = "hash",
            role = UserRole.ADMIN.name
        )
        val exception = Exception("Database error")

        every { userRepository.getUser(adminId) } returns Result.success(adminUser)
        every { userRepository.deleteUser(userToDeleteId) } throws exception

        // When
        val result = deleteUserUseCase.invoke(adminId, userToDeleteId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}