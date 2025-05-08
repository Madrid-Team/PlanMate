package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import data.repository.UserRepositoryImpl
import data.source.user.UserDataSource
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class CreateUserUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var userDataSource: UserDataSource

    @BeforeEach
    fun setUp() {
        // Mock the data source
        userDataSource = mockk<UserDataSource>(relaxed = false)
        userRepository = UserRepositoryImpl(userDataSource)
        mockkConstructor(ValidateUser::class)
        createUserUseCase = CreateUserUseCase(userRepository)
    }

    @Test
    fun `should create user successfully When User dose not exists before`() {
        // Given
        val newUserName = "new user"
        val user = User(id = UUID.randomUUID(), username = newUserName, passwordHash = "password", role = "MATE")
        val generateId = UUID.randomUUID()

        every { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns generateId

        every { userDataSource.getUserByName(newUserName) } throws UserExceptions.UserNotFoundException()

        every { userDataSource.createNewUser(any()) } returns Unit

        // When
        assertDoesNotThrow {
            createUserUseCase.createUser(user)
        }

        // Then
        verify {
            userDataSource.createNewUser(user.copy(id = generateId))
        }
    }

    @Test
    fun `Should not create user When user already exists`() {
        // Given
        val existingUsername = "existingUser"
        val existingUser = User(
            id = UUID.randomUUID(),
            username = existingUsername,
            passwordHash = "password",
            role = "MATE"
        )

        every { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns UUID.randomUUID()

        every { userDataSource.getUserByName(existingUsername) } returns existingUser

        every { userDataSource.createNewUser(any()) } throws UserExceptions.UserExist("User already exists")

        // When & Then
        val exception = assertThrows<UserExceptions.UserExist> {
            createUserUseCase.createUser(existingUser)
        }

        assertThat(exception.message).isEqualTo("User already exists")

        // Verify createNewUser was called
        verify {
            userRepository.createNewUser(any())
        }
    }
}