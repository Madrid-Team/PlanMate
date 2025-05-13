package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import data.utils.PasswordHasher
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utils.UserExceptions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.BeforeTest

class LoginUserUseCaseTest {
//    private lateinit var loginUser: LoginUserUseCase
//    private lateinit var userRepository: UserRepository
//
//    @BeforeTest
//    fun setUp() {
//        userRepository = mockk()
//        loginUser = LoginUserUseCase(userRepository)
//    }
//
//    @Test
//    fun `Should login user successfully when username and password are correct`() {
//        runTest {
//            // Given
//            val user = User(id = UUID.randomUUID(), "user1", "pass123", "MATE")
//            coEvery { userRepository.getAllUsers() } returns listOf(user)
//
//            // When
//            val result = loginUser.login("user1", "pass123")
//
//            // Then
//            assertThat(result).isInstanceOf(User::class.java)
//            assertTrue(result.username == "user1")
//        }
//    }
//
//    @Test
//    fun `Should not login when username or password are incorrect`() {
//        runTest {
//            // Given
//            val user = User(id = UUID.randomUUID(), "user1", PasswordHasher.hash("correctPass"), "MATE")
//            coEvery { userRepository.getAllUsers() } returns listOf(user)
//
//            // When
//            assertThrows<UserExceptions.WrongPasswordOrUserName> {
//                loginUser.login("user1", "wrongPass")
//            } // Should throw
//        }
//    }
//
//
//    @Test
//    fun `Shouldn't login When username or password is empty`() {
//        runTest {
//            // Given
//            val user = User(id = UUID.randomUUID(), "MATE_1", PasswordHasher.hash("PASSWORD_HASH_1"), "MATE")
//
//            coEvery { userRepository.createNewUser(user) } returns Unit
//
//            // When && Then
//            assertThrows<UserExceptions.UserNameOrPasswordError> { loginUser.login("", "PASSWORD_HASH_1") }
//        }
//    }
}