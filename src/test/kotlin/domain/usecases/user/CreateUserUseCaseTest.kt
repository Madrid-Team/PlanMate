package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class CreateUserUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase
    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        mockkConstructor(ValidateUser::class)
        createUserUseCase = CreateUserUseCase(userRepository)
    }


    @Test
    fun `should create user successfully When User dose not exists before`() {
        // Given
        val newUserName = "new user"
        val user = User(id = UUID.randomUUID(), username = newUserName, passwordHash = "password", role = "MATE")
        val generateId = UUID.randomUUID()
        val newUser = user.copy(id = generateId)

        every { userRepository.getUserByName(newUserName) } returns null
        every { userRepository.createNewUser(newUser) } returns Unit
        every { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns generateId

        // When - Should not throw any exceptions
        createUserUseCase.createUser(user)

        // Then
        verify {
            userRepository.getUserByName(newUserName)
            userRepository.createNewUser(newUser)
        }
    }

    @Test
    fun `Should not create user When user already exists`() {
        // Given
        val existingUser = User(id = UUID.randomUUID(), "username7", "password", "ADMIN")

        every { userRepository.getUserByName("username7") } returns existingUser

        // When/Then
        val exception = assertThrows<UserExceptions.UserExist> {
            createUserUseCase.createUser(existingUser)
        }

        assertThat(exception.message).contains("User already exists")
    }
}