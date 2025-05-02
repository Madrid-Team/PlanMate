package domain.repository

import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import data.utils.PasswordHasher
import domain.usecases.LoginUserUseCase
import domain.utlis.UserException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class LoginUserTest {
    private lateinit var loginUser: LoginUserUseCase
    private lateinit var userRepository: UserRepository
    @BeforeTest
    fun setUp() {
        userRepository= mockk()
        loginUser = LoginUserUseCase(userRepository)
    }
    @Test
    fun `Should login user successfully when username and password are correct`() {
        // Given
        val user = UserDto("1", "user1", PasswordHasher.hash("pass123"), UserRoleDto.MATE)
        every { userRepository.getAllUsers() } returns Result.success(listOf(user))

        // When
        val result = loginUser.invoke("user1", "pass123")

        // Then
        assertTrue { result != null }
    }


    @Test
    fun `Should not login when username or password are incorrect`() {
        // Given
        val user = UserDto("1", "user1", PasswordHasher.hash("correctPass"), UserRoleDto.MATE)
        every { userRepository.getAllUsers() } returns Result.success(listOf(user))

        // When
        assertThrows <UserException.WrongPasswordOrUserName> {
            loginUser.invoke("user1", "wrongPass")
        } // Should throw
    }



    @Test
    fun `Shouldn't login When username or password is null`(){
        // Given
        val mate = UserDto(id = "1","MATE_1","PASSWORD_HASH_1", UserRoleDto.MATE)

        every {   userRepository.addUser(mate)} returns Result.success(Unit)

        // When && Then
        assertThrows <UserException.NotFoundUser> { loginUser.invoke(null, "PASSWORD_HASH_1") }
    }


}