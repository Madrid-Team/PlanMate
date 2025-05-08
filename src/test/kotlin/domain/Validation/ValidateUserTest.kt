package domain.Validation

import com.google.common.truth.Truth.assertThat
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions
import domain.validation.ValidateUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidateUserTest {
    private lateinit var userRepository: UserRepository
    private lateinit var validateUser: ValidateUser

    @BeforeTest
    fun setup() {
        userRepository = mockk(relaxUnitFun = true)
        mockkConstructor(ValidateUser::class)
        validateUser = ValidateUser(userRepository)
    }


    @Test
    fun `Should validate user successfully When user is valid`() {
        //Given
        val userValid = User(
            id = UUID.randomUUID(), "username7",
            "#yty", "ADMIN"
        )
        //When
        every {
            validateUser.validateUserToLogin(
                listOf(userValid), userValid.username, userValid.passwordHash
            )
        } returns userValid

        val result = validateUser.validateUserToLogin(
            listOf(userValid), userValid.username, userValid.passwordHash
        )
        assertEquals(userValid, result)
    }

    @Test
    fun `should throw WrongPasswordOrUserName when password is incorrect`() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "john",
            passwordHash = "hashed123",
            role = "ADMIN"
        )
        val users = listOf(user)

        // When
        val exception = assertThrows<UserExceptions> {
            validateUser.validateUserToLogin(users, user.username, "user")
        }

        //Then
        assertTrue(exception is UserExceptions.WrongPasswordOrUserName)
    }

    @Test
    fun `should throw NotFoundUser when users list is empty `() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "john",
            passwordHash = "hashed123",
            role = "ADMIN"
        )
        val users = emptyList<User>()


        // When
        val exception = assertThrows<UserExceptions> {
            validateUser.validateUserToLogin(users, user.username, user.passwordHash)
        }

        //Then
        assertTrue(exception is UserExceptions.UserNotFoundException)
    }


    @Test
    fun `should throw NotFoundUser when username does not exist`() {
        // Given
        val user = User(
            id = UUID.randomUUID(),
            username = "john",
            passwordHash = "hashed123",
            role = "ADMIN"
        )
        val users = listOf(user)

        // When + Then
        val exception = assertThrows<UserExceptions.UserNotFoundException> {
            validateUser.validateUserToLogin(users, "nonexistentUser", "hashed123")
        }

        assertThat(exception.message).isEqualTo("Not found user or wrong username")
    }

    @Test
    fun `should generate a unique UUID When UUID is in use`() {
        // Given
        val newUUID = UUID.randomUUID()
        every { userRepository.getUserById(any()) } returns mockk()

        // When
        val generatedUUID = validateUser.generateUUIDValidToNewUser()

        // Then
        assertThat(generatedUUID).isNotEqualTo(newUUID)
        verify(exactly = 1) { userRepository.getUserById(any()) }
    }

}