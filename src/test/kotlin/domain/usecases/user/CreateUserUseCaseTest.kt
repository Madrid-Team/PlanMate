package domain.usecases.user

import domain.models.authentication.User
import domain.repository.UserRepository
import domain.usecases.user.*
import domain.utils.UserExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var passwordHashUseCase: PasswordHashUseCase
    private lateinit var createUserUseCase: CreateUserUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        validateNameUseCase = mockk()
        validatePasswordUseCase = mockk()
        passwordHashUseCase = mockk()
        createUserUseCase = CreateUserUseCase(
            userRepository,
            validateNameUseCase,
            validatePasswordUseCase,
            passwordHashUseCase
        )
    }

    @Test
    fun `should create new user when input is valid`() = runTest {
        // Given
        val username = "ValidUser"
        val password = "ValidPassword123"
        val hashedPassword = "hashedPassword"

        every { validateNameUseCase.validateName(username) } returns Unit
        every { validatePasswordUseCase.validatePassword(password) } returns Unit
        every { passwordHashUseCase.passwordHash(password) } returns hashedPassword
        coEvery { userRepository.createNewUser(any()) } just Runs

        // When
        createUserUseCase.createUser(username, password)

        // Then
        coVerify(exactly = 1) {
            userRepository.createNewUser(
                match {
                    it.username == username &&
                            it.passwordHash == hashedPassword &&
                            it.role == "MATE"
                }
            )
        }
    }

    @Test
    fun `should throw InvalidUserName when name is invalid`() = runTest {
        // Given
        val username = ""
        val password = "ValidPassword123"

        every { validateNameUseCase.validateName(username) } throws UserExceptions.InvalidUserName()

        // When & Then
        assertThrows<UserExceptions.InvalidUserName> {
            createUserUseCase.createUser(username, password)
        }

        coVerify(exactly = 0) { userRepository.createNewUser(any()) }
    }

    @Test
    fun `should throw InvalidPasswordError when password is invalid`() = runTest {
        // Given
        val username = "ValidUser"
        val password = ""

        every { validateNameUseCase.validateName(username) } returns Unit
        every { validatePasswordUseCase.validatePassword(password) } throws UserExceptions.InvalidPasswordError()

        // When & Then
        assertThrows<UserExceptions.InvalidPasswordError> {
            createUserUseCase.createUser(username, password)
        }

        coVerify(exactly = 0) { userRepository.createNewUser(any()) }
    }
}
