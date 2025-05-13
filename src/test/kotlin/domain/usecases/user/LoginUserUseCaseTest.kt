package domain.usecases.user

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.UserExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class LoginUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var loginUserUseCase: LoginUserUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        validateNameUseCase = mockk()
        validatePasswordUseCase = mockk()
        loginUserUseCase = LoginUserUseCase(
            userRepository,
            validateNameUseCase,
            validatePasswordUseCase
        )
    }

    @Test
    fun `throws InvalidUserName when name is invalid`() = runTest {
        // Given
        val invalidName = "ab"
        every { validateNameUseCase.validateName(invalidName) } throws UserExceptions.InvalidUserName()

        // When & Then
        assertThrows<UserExceptions.InvalidUserName> {
            loginUserUseCase.login(invalidName, "123456")
        }
    }

    @Test
    fun `throws InvalidPasswordError when password is invalid`() = runTest {
        // Given
        val name = "mohamed"
        val invalidPassword = "123"
        every { validateNameUseCase.validateName(name) } returns Unit
        every { validatePasswordUseCase.validatePassword(invalidPassword) } throws UserExceptions.InvalidPasswordError()

        // When & Then
        assertThrows<UserExceptions.InvalidPasswordError> {
            loginUserUseCase.login(name, invalidPassword)
        }
    }

    @Test
    fun `returns user when credentials are valid`() = runTest {
        // Given
        val name = "mohamed"
        val password = "123456"
        val passwordHash = PasswordHasher.hash(password)
        val id = UUID.randomUUID()
        val user = User(id = id, name, passwordHash,role = "USER")

        every { validateNameUseCase.validateName(name) } returns Unit
        every { validatePasswordUseCase.validatePassword(password) } returns Unit
        coEvery { userRepository.login(name, any()) } returns user

        // When
        val result = loginUserUseCase.login(name, password)

        // Then
        assertEquals(user, result)
        coVerify(exactly = 1) { userRepository.login(name, any()) }
    }
}

