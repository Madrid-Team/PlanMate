package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LoginUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var loginUserUseCase: LoginUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        validateNameUseCase = mockk()
        validatePasswordUseCase = mockk()
        loginUserUseCase = LoginUserUseCase(userRepository, validateNameUseCase, validatePasswordUseCase)
    }

    @Test
    fun `should login successfully with valid username and password`() = runTest {
        // Given
        val username = "mohamed"
        val password = "12345678"
        val passwordHash = PasswordHasher.hash(password)
        val user = User(UUID.randomUUID(), username, passwordHash, UserRole.MATE.name)

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.Valid
        coEvery { validatePasswordUseCase.validatePassword(password) } returns PasswordValidationResult.Valid
        coEvery { userRepository.login(username, passwordHash) } returns user

        // When
        val result = loginUserUseCase.login(username, password)

        // Then
        assertThat(result).isEqualTo(user)
        coVerify(exactly = 1) { userRepository.login(username, passwordHash) }
    }

    @Test
    fun `should throw InvalidUserName when name is not valid`() = runTest {
        // Given
        val username = "mo"
        val password = "12345678"

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.NotValid("Too short")

        // When & Then
        assertFailsWith<UserExceptions.InvalidUserName> {
            loginUserUseCase.login(username, password)
        }
    }

    @Test
    fun `should throw InvalidPasswordError when password is not valid`() = runTest {
        // Given
        val username = "mohamed"
        val password = ""

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.Valid
        coEvery { validatePasswordUseCase.validatePassword(password) } returns PasswordValidationResult.NotValid("Password cannot be empty")

        // When & Then
        assertFailsWith<UserExceptions.InvalidPasswordError> {
            loginUserUseCase.login(username, password)
        }
    }
}
