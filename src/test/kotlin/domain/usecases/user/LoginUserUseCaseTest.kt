package domain.usecases.user

import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.BeforeTest

class LoginUserUseCaseTest {
    private lateinit var loginUser: LoginUserUseCase
    private lateinit var userRepository: UserRepository

    @BeforeTest
    fun setUp() {
        userRepository = mockk()
        loginUser = LoginUserUseCase(userRepository)
    }

    @Test
    fun `Should login user successfully when username and password are correct`() {
        // Given
        val user = User(id = UUID.randomUUID(), "user1", "pass123", "MATE")
        every { userRepository.getAllUsers() } returns listOf(user)

        // When
        val result = loginUser.invoke("user1", "pass123")

        // Then
        assertTrue { result != null }
    }

    @Test
    fun `Should not login when username or password are incorrect`() {
        // Given
        val user = User(id = UUID.randomUUID(), "user1", PasswordHasher.hash("correctPass"), "MATE")
        every { userRepository.getAllUsers() } returns listOf(user)

        // When
        assertThrows<UserExceptions.WrongPasswordOrUserName> {
            loginUser.invoke("user1", "wrongPass")
        } // Should throw
    }


    @Test
    fun `Shouldn't login When username or password is null`() {
        // Given
        val user = User(id = UUID.randomUUID(), "MATE_1", PasswordHasher.hash("PASSWORD_HASH_1"), "MATE")

        every { userRepository.createNewUser(user) } returns Unit

        // When && Then
        assertThrows<UserExceptions.NotFoundUser> { loginUser.invoke(null, "PASSWORD_HASH_1") }
    }
}