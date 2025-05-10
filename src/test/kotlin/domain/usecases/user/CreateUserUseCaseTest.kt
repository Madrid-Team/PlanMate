package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import data.mapper.toDto
import data.repository.UserRepositoryImpl
import data.source.user.UserExternalDataSource
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser
import io.mockk.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class CreateUserUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var userExternalDataSource: UserExternalDataSource
    private var testScope = TestScope()

    @BeforeEach
    fun setUp() {
        // Mock the data source
        userExternalDataSource = mockk<UserExternalDataSource>(relaxed = false)
        userRepository = UserRepositoryImpl(userExternalDataSource)
        mockkConstructor(ValidateUser::class)
        createUserUseCase = CreateUserUseCase(userRepository)
    }

    @Test
    fun `should create user successfully When User dose not exists before`() {
        runTest {
            // Given
            val newUserName = "new user"
            val user = User(id = UUID.randomUUID(), username = newUserName, passwordHash = "password", role = "MATE")
            val generateId = UUID.randomUUID()

            coEvery { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns generateId

            coEvery { userExternalDataSource.getUserByName(newUserName) } throws UserExceptions.UserNotFoundException()

            coEvery { userExternalDataSource.createNewUser(any()) } returns Unit

            // When
            assertDoesNotThrow {
                createUserUseCase(user)
            }

            // Then
            coVerify {
                userExternalDataSource.createNewUser(user.copy(id = generateId).toDto())
            }
        }
    }

    @Test
    fun `Should not create user When user already exists`() {
        runTest {
            // Given
            val existingUsername = "existingUser"
            val existingUser = User(
                id = UUID.randomUUID(),
                username = existingUsername,
                passwordHash = "password",
                role = "MATE"
            )

            coEvery { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns UUID.randomUUID()

            coEvery { userExternalDataSource.getUserByName(existingUsername) } returns existingUser.toDto()

            coEvery { userExternalDataSource.createNewUser(any()) } throws UserExceptions.UserExist("User already exists")

            // When & Then
            val exception = assertThrows<UserExceptions.UserExist> {
                createUserUseCase(existingUser)
            }

            assertThat(exception.message).isEqualTo("User already exists")

            // Verify createNewUser was called
            testScope.launch {
                coVerify {
                    userRepository.createNewUser(any())
                }
            }
        }
    }
}