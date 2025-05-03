package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.validation.ValidateUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
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

        every { userRepository.getUserByName(newUserName) } returns Result.success(null)
        every { userRepository.createNewUser(newUser) } returns Result.success(Unit)
        every { anyConstructed<ValidateUser>().generateUUIDValidToNewUser() } returns generateId

        // When
        val result = createUserUseCase.createUser(user)

        // Then
        assertThat(result.isSuccess).isTrue()
        verify {
            userRepository.getUserByName(newUserName)
            userRepository.createNewUser(newUser)
        }
    }

    @Test
    fun `Should not create user When  wrong formed user from repository`() {
        // Given
        val malformedUser = User(id = UUID.randomUUID(), "username7", "", "ADMIN")

        every { userRepository.getUserByName("username7") } returns Result.success(malformedUser)

        // When
        val result = createUserUseCase.createUser(malformedUser)

        // Then
        assertThat(result.isFailure).isTrue()

    }
}