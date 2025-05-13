package domain.usecases.user

import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository

import domain.utils.AdminValidationResult
import domain.utils.UserExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validateAdminRoleUseCase: ValidateAdminRoleUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        validateAdminRoleUseCase = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository, validateAdminRoleUseCase)
    }

    @Test
    fun `should delete user when requester is admin`() = runTest {
        val adminId = UUID.randomUUID().toString()
        val userToDeleteId = UUID.randomUUID().toString()

        val adminUser = User(
            id = UUID.fromString(adminId),
            username = "admin_user",
            passwordHash = "hashed_password",
            role = UserRole.ADMIN.name
        )

        coEvery { userRepository.getUserById(adminId) } returns adminUser
        every { validateAdminRoleUseCase.validate(adminUser.role) } returns AdminValidationResult.Valid
        coEvery { userRepository.deleteUser(userToDeleteId) } just Runs

        deleteUserUseCase.deleteUser(adminId, userToDeleteId)

        coVerify(exactly = 1) { userRepository.deleteUser(userToDeleteId) }
    }

    @Test
    fun `should throw exception when requester is not admin`() = runTest {
        val mateId = UUID.randomUUID().toString()
        val userToDeleteId = UUID.randomUUID().toString()

        val mateUser = User(
            id = UUID.fromString(mateId),
            username = "mate_user",
            passwordHash = "hashed_password",
            role = UserRole.MATE.name
        )

        coEvery { userRepository.getUserById(mateId) } returns mateUser
        every { validateAdminRoleUseCase.validate(mateUser.role) } returns AdminValidationResult.NotAdmin

        assertFailsWith<UserExceptions.UserNotAdminException> {
            deleteUserUseCase.deleteUser(mateId, userToDeleteId)
        }

        coVerify(exactly = 0) { userRepository.deleteUser(any()) }
    }
}
