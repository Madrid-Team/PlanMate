package domain.usecases.user
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.UserNotAdminException
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validateAdminRoleUseCase: ValidateAdminRoleUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        validateAdminRoleUseCase = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository, validateAdminRoleUseCase)
    }

    @Test
    fun `should delete user when requester is admin`() = runTest {
        // Given
        val requesterId = UUID.randomUUID()
        val targetId = UUID.randomUUID()
        val adminUser = User(
            id = requesterId,
            username = "AdminUser",
            passwordHash = "hashedPassword",
            role = "ADMIN"
        )

        coEvery { userRepository.getUserByUserId(requesterId.toString()) } returns adminUser
        every { validateAdminRoleUseCase.validate("ADMIN") } returns true
        coEvery { userRepository.deleteUserByUserId(targetId.toString()) } just Runs

        // When
        deleteUserUseCase.deleteUser(requesterId.toString(), targetId.toString())

        // Then
        coVerify(exactly = 1) { userRepository.deleteUserByUserId(targetId.toString()) }
    }

    @Test
    fun `should throw UserNotAdminException when requester is not admin`() = runTest {
        // Given
        val requesterId = UUID.randomUUID()
        val targetId = UUID.randomUUID()
        val normalUser = User(
            id = requesterId,
            username = "NormalUser",
            passwordHash = "hashedPassword",
            role = "USER"
        )

        coEvery { userRepository.getUserByUserId(requesterId.toString()) } returns normalUser
        every { validateAdminRoleUseCase.validate("USER") } returns false

        // When & Then
        assertThrows<UserNotAdminException> {
            deleteUserUseCase.deleteUser(requesterId.toString(), targetId.toString())
        }

        coVerify(exactly = 0) { userRepository.deleteUserByUserId(any()) }
    }
}
