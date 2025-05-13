package domain.usecases.user

import domain.models.authentication.UserRole
import domain.repository.UserRepository
import domain.utils.NameValidationResult
import domain.utils.PasswordValidationResult
import domain.utils.UserExceptions
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CreateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var validateNameUseCase: ValidateNameUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var passwordHashUseCase: PasswordHashUseCase

    @BeforeEach
    fun setUp() {
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
    fun `should call create user successfully when the username and password are correct`() = runTest {
        val username = "mohamed"
        val password = "123456789"
        val passwordHash = "hashed_password"

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.Valid
        coEvery { validatePasswordUseCase.validatePassword(password) } returns PasswordValidationResult.Valid
        coEvery { passwordHashUseCase.passwordHash(password) } returns passwordHash
        coEvery { userRepository.createNewUser(any()) } just Runs

        createUserUseCase.createUser(username, password)

        coVerify(exactly = 1) {
            userRepository.createNewUser(
                match {
                    it.username == username &&
                            it.passwordHash == passwordHash &&
                            it.role == UserRole.MATE.name
                }
            )
        }
    }

    @Test
    fun `should throw invalid username exception when username is empty`() = runTest {
        val username = ""
        val password = "123456789"

        coEvery {
            validateNameUseCase.validateName(username)
        } returns NameValidationResult.NotValid(
            UserExceptions.EmptyUserNameException().message.toString()
        )

        coEvery { validatePasswordUseCase.validatePassword(password) } returns PasswordValidationResult.Valid

        assertFailsWith<UserExceptions.InvalidUserName> {
            createUserUseCase.createUser(username, password)
        }
    }

    @Test
    fun `should throw invalid username exception when username is less than 3`() = runTest {
        val username = "mo"
        val password = "123456789"

        coEvery {
            validateNameUseCase.validateName(username)
        } returns NameValidationResult.NotValid(
            UserExceptions.UserNameLessThan3CharsException().message.toString()
        )

        coEvery { validatePasswordUseCase.validatePassword(password) } returns PasswordValidationResult.Valid

        assertFailsWith<UserExceptions.InvalidUserName> {
            createUserUseCase.createUser(username, password)
        }
    }

    @Test
    fun `should throw invalid password exception when password is empty`() = runTest {
        val username = "kamel"
        val password = ""

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.Valid
        coEvery {
            validatePasswordUseCase.validatePassword(password)
        } returns PasswordValidationResult.NotValid(
            UserExceptions.EmptyPasswordException().message.toString()
        )

        assertFailsWith<UserExceptions.InvalidPasswordError> {
            createUserUseCase.createUser(username, password)
        }
    }

    @Test
    fun `should throw invalid password exception when password is less than 6`() = runTest {
        val username = "mohamed"
        val password = "1234"

        coEvery { validateNameUseCase.validateName(username) } returns NameValidationResult.Valid
        coEvery {
            validatePasswordUseCase.validatePassword(password)
        } returns PasswordValidationResult.NotValid(
            UserExceptions.PasswordLessThan6CharsException().message.toString()
        )

        assertFailsWith<UserExceptions.InvalidPasswordError> {
            createUserUseCase.createUser(username, password)
        }
    }
}
